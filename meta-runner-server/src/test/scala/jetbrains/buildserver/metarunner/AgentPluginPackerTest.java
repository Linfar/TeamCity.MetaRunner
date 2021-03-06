/*
 * Copyright 2000-2010 JetBrains s.r.o.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package jetbrains.buildserver.metarunner;

import jetbrains.buildServer.BaseTestCase;
import jetbrains.buildserver.metarunner.agent.AgentPluginLibrariesLocator;
import jetbrains.buildserver.metarunner.agent.AgentPluginPacker;
import jetbrains.buildserver.metarunner.xml.RunnerSpec;
import org.jetbrains.annotations.NotNull;
import org.jmock.Expectations;
import org.jmock.Mockery;
import org.testng.Assert;
import org.testng.annotations.Test;

import java.io.File;
import java.io.IOException;
import java.util.Collection;
import java.util.Collections;
import java.util.Enumeration;
import java.util.TreeSet;
import java.util.zip.ZipEntry;
import java.util.zip.ZipFile;

/**
 * @author Eugene Petrenko (eugene.petrenko@jetbrains.com)
 *         10.12.10 15:23
 */
@Test
public class AgentPluginPackerTest extends BaseTestCase {
  @Test
  public void test_01() throws IOException {
    final File home = createTempDir();
    final File packFile = new File(home, "zip/pack.zip");
    final File metadataPath = new File(home, "defs"){{
      new File(this, "folder"){{
        mkdirs();
        new File(this, "aaa.txt").createNewFile();}};
    }};
    final File libPath = new File(home, "zzz"){{
      new File(this, "libF"){{
        mkdirs();
        new File(this, "bbb.txt").createNewFile();}};
    }};

    AgentPluginPacker p = new AgentPluginPacker(new AgentPluginLibrariesLocator(){
      @NotNull
      public File getAgentLibs() {
        return libPath;
      }
    });

    Mockery m = new Mockery();
    final RunnerSpec sp = m.mock(RunnerSpec.class);
    m.checking(new Expectations(){{
      allowing(sp).runType(); will(returnValue("folder"));
      allowing(sp).getMetaRunnerRoot(); will(returnValue(metadataPath));
    }});

     p.packPlugin(packFile, Collections.singletonList(sp));
    Assert.assertEquals(packFile, packFile);
    Assert.assertTrue(packFile.isFile());

    ZipFile zip = new ZipFile(packFile);
    Collection<String> entries = new TreeSet<String>();
    final Enumeration<? extends ZipEntry> e = zip.entries();
    while(e.hasMoreElements()) {
      entries.add(e.nextElement().getName());
    }

    System.out.println("entries = " + entries);
    Assert.assertTrue(entries.contains("meta-runner/lib/libF/bbb.txt"));
    Assert.assertTrue(entries.contains("meta-runner/meta-runners/folder/folder/aaa.txt"));
  }
}
