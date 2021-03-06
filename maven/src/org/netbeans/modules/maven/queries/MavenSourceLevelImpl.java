/*
 * Licensed to the Apache Software Foundation (ASF) under one
 * or more contributor license agreements.  See the NOTICE file
 * distributed with this work for additional information
 * regarding copyright ownership.  The ASF licenses this file
 * to you under the Apache License, Version 2.0 (the
 * "License"); you may not use this file except in compliance
 * with the License.  You may obtain a copy of the License at
 *
 *   http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing,
 * software distributed under the License is distributed on an
 * "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY
 * KIND, either express or implied.  See the License for the
 * specific language governing permissions and limitations
 * under the License.
 */

package org.netbeans.modules.maven.queries;

import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.io.File;
import java.net.URI;
import java.util.Arrays;
import java.util.Iterator;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import javax.swing.event.ChangeListener;
import org.apache.maven.artifact.versioning.DefaultArtifactVersion;
import org.codehaus.plexus.component.configurator.expression.ExpressionEvaluator;
import org.codehaus.plexus.util.xml.Xpp3Dom;
import org.netbeans.api.java.queries.SourceLevelQuery;
import org.netbeans.api.project.Project;
import org.netbeans.modules.maven.NbMavenProjectImpl;
import org.netbeans.modules.maven.api.Constants;
import org.netbeans.modules.maven.api.NbMavenProject;
import org.netbeans.modules.maven.api.PluginPropertyUtils;
import org.netbeans.spi.java.queries.SourceLevelQueryImplementation2;
import org.netbeans.spi.project.ProjectServiceProvider;
import org.openide.filesystems.FileObject;
import org.openide.filesystems.FileUtil;
import org.openide.util.ChangeSupport;
import org.openide.util.Utilities;
import org.openide.util.WeakListeners;

/**
 * maven implementation of SourceLevelQueryImplementation.
 * checks a property of maven-compiler-plugin
 * @author Milos Kleint
 */
@ProjectServiceProvider(service=SourceLevelQueryImplementation2.class, projectType="org-netbeans-modules-maven")
public class MavenSourceLevelImpl implements SourceLevelQueryImplementation2 {
    
    private static final Logger LOGGER = Logger.getLogger(MavenSourceLevelImpl.class.getName());
    
    static final Pattern PROFILE = Pattern.compile("-profile (compact1|compact2|compact3){1}?");
    private final Project project;

    public MavenSourceLevelImpl(Project proj) {
        project = proj;
    }
    
    private String getSourceLevelString(FileObject javaFile) {
        File file = FileUtil.toFile(javaFile);
        if (file == null) {
            //#128609 something in jar?
            return null;
        }
        URI uri = Utilities.toURI(file);
        assert "file".equals(uri.getScheme());
        String goal = "compile"; //NOI18N
        String property = "maven.compiler.source";
        String param = Constants.SOURCE_PARAM;
        NbMavenProjectImpl nbprj = project.getLookup().lookup(NbMavenProjectImpl.class);
        for (URI testuri : nbprj.getSourceRoots(true)) {
            if (uri.getPath().startsWith(testuri.getPath())) {
                goal = "testCompile"; //NOI18N
                property = "maven.compiler.testSource";
                param = "testSource";
            }
        }
        for (URI testuri : nbprj.getGeneratedSourceRoots(true)) {
            if (uri.getPath().startsWith(testuri.getPath())) {
                goal = "testCompile"; //NOI18N
                property = "maven.compiler.testSource";
                param = "testSource";
            }
        }
        String sourceLevel = PluginPropertyUtils.getPluginProperty(project, Constants.GROUP_APACHE_PLUGINS,  //NOI18N
                                                              Constants.PLUGIN_COMPILER,  //NOI18N
                                                              param,  //NOI18N
                                                              goal,
                                                              property);
        if (sourceLevel != null) {
            return sourceLevel;
        }
        if ("testCompile".equals(goal)) { //#237986 in tests, first try "testSource" param, then fallback to "source"
            sourceLevel = PluginPropertyUtils.getPluginProperty(project, Constants.GROUP_APACHE_PLUGINS,  //NOI18N
                                                              Constants.PLUGIN_COMPILER,  //NOI18N
                                                              Constants.SOURCE_PARAM,  //NOI18N
                                                              "testCompile",
                                                              "maven.compiler.source");            
            if (sourceLevel != null) {
                return sourceLevel;
            }
        }
        
        
        String version = PluginPropertyUtils.getPluginVersion(
                nbprj.getOriginalMavenProject(),
                Constants.GROUP_APACHE_PLUGINS, Constants.PLUGIN_COMPILER);
        if (version == null || new DefaultArtifactVersion(version).compareTo(new DefaultArtifactVersion("2.3")) >= 0) {
            return "1.5";
        } else {
            return "1.3";
        }
    }
    
    private SourceLevelQuery.Profile getSourceProfile(FileObject javaFile) {
        File file = FileUtil.toFile(javaFile);
        if (file == null) {
            //#128609 something in jar?
            return SourceLevelQuery.Profile.DEFAULT;
        }
        URI uri = Utilities.toURI(file);
        assert "file".equals(uri.getScheme());
        String goal = "compile"; //NOI18N
        NbMavenProjectImpl nbprj = project.getLookup().lookup(NbMavenProjectImpl.class);
        for (URI testuri : nbprj.getSourceRoots(true)) {
            if (uri.getPath().startsWith(testuri.getPath())) {
                goal = "testCompile"; //NOI18N
            }
        }
        for (URI testuri : nbprj.getGeneratedSourceRoots(true)) {
            if (uri.getPath().startsWith(testuri.getPath())) {
                goal = "testCompile"; //NOI18N
            }
        }
        //compilerArguments vs compilerArgument vs compilerArgs - all of them get eventually merged in compiler mojo..
        //--> all need to be checked.
        String args = PluginPropertyUtils.getPluginProperty(project, Constants.GROUP_APACHE_PLUGINS,  //NOI18N
                                                              Constants.PLUGIN_COMPILER,  //NOI18N
                                                              "compilerArgument",  //NOI18N
                                                              goal,
                                                              null);
        if (args != null) {
            Matcher match = PROFILE.matcher(args);
            if (match.find()) {
                String prof = match.group(1);
                SourceLevelQuery.Profile toRet = SourceLevelQuery.Profile.forName(prof);
                return toRet != null ? toRet : SourceLevelQuery.Profile.DEFAULT;
            }
        }
        
        
        String compilerArgumentsProfile = PluginPropertyUtils.getPluginPropertyBuildable(project, 
                Constants.GROUP_APACHE_PLUGINS,
                Constants.PLUGIN_COMPILER, //NOI18N
                goal,
                new ConfigBuilder());
               
        if (compilerArgumentsProfile != null) {
            SourceLevelQuery.Profile toRet = SourceLevelQuery.Profile.forName(compilerArgumentsProfile);
            return toRet != null ? toRet : SourceLevelQuery.Profile.DEFAULT;
        }
        String[] compilerArgs = PluginPropertyUtils.getPluginPropertyList(project, Constants.GROUP_APACHE_PLUGINS,
                         Constants.PLUGIN_COMPILER, //NOI18N
                         "compilerArgs", "arg", goal);
        if (compilerArgs != null) {
            Iterator<String> it = Arrays.asList(compilerArgs).iterator();
            while (it.hasNext()) {
                String p = it.next();
                if ("-profile".equals(p) && it.hasNext()) {               
                    String prof = it.next();
                    SourceLevelQuery.Profile toRet = SourceLevelQuery.Profile.forName(prof);
                    return toRet != null ? toRet : SourceLevelQuery.Profile.DEFAULT;
                }
            }
        }
        return SourceLevelQuery.Profile.DEFAULT;
    }

    @Override public Result getSourceLevel(final FileObject javaFile) {
        return new ResultImpl(javaFile);
    }
    
    private static class ConfigBuilder implements PluginPropertyUtils.ConfigurationBuilder<String> {

        @Override
        public String build(Xpp3Dom configRoot, ExpressionEvaluator eval) {
            if (configRoot != null) {
                Xpp3Dom args = configRoot.getChild("compilerArguments");
                if (args != null) {
                    Xpp3Dom prof = args.getChild("profile");
                    if (prof != null) {
                        return prof.getValue();
                    }
                }
            }
            return null;
        }
        
    }
    
    private class ResultImpl implements SourceLevelQueryImplementation2.Result2, PropertyChangeListener {
        
        private final FileObject javaFile;
        private final ChangeSupport cs = new ChangeSupport(this);
        private final PropertyChangeListener pcl = WeakListeners.propertyChange(this, project.getLookup().lookup(NbMavenProject.class));
        private String cachedLevel = null;
        private SourceLevelQuery.Profile cachedProfile;
        private final Object CACHE_LOCK = new Object();
        
        ResultImpl(FileObject javaFile) {
            this.javaFile = javaFile;
            project.getLookup().lookup(NbMavenProject.class).addPropertyChangeListener(pcl);
        }

        @Override public String getSourceLevel() {
            synchronized (CACHE_LOCK) {
                if (cachedLevel == null) {
                    cachedLevel = getSourceLevelString(javaFile);
                }
                if(LOGGER.isLoggable(Level.FINE)) {
                    LOGGER.log(Level.FINE, "MavenSourceLevelQuery: {0} level {1}", new Object[] {javaFile.getPath(), cachedLevel}); // NOI18N
                }
                return cachedLevel;
            }
        }

        @Override public void addChangeListener(ChangeListener listener) {
            cs.addChangeListener(listener);
        }

        @Override public void removeChangeListener(ChangeListener listener) {
            cs.removeChangeListener(listener);
        }

        @Override public void propertyChange(PropertyChangeEvent evt) {
            if (NbMavenProject.PROP_PROJECT.equals(evt.getPropertyName())) {
                Project p = (Project) evt.getSource();
                if (p.getLookup().lookup(NbMavenProject.class).isUnloadable()) {
                    return; //let's just continue with the old value, rescanning classpath for broken project and re-creating it later serves no greater good.
                }
                synchronized (CACHE_LOCK) {
                    cachedLevel = null;
                    cachedProfile = null;
                }
                if(LOGGER.isLoggable(Level.FINER)) {
                    LOGGER.log(Level.FINER, "MavenSourceLevelQuery: {0} fire change", javaFile.getPath()); // NOI18N
                }
                cs.fireChange();
            }
        }

        @Override
        public SourceLevelQuery.Profile getProfile() {
            synchronized (CACHE_LOCK) {
                if (cachedProfile == null) {
                    cachedProfile = getSourceProfile(javaFile);
                }
                if(LOGGER.isLoggable(Level.FINE)) {
                    LOGGER.log(Level.FINE, "MavenSourceLevelQuery: {0} profile {1}", new Object[] {javaFile.getPath(), cachedProfile});
                }
                return cachedProfile;
            }
        }

    }
    
}
