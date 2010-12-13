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

package jetbrains.buildserver.metarunner.xml;

import org.jetbrains.annotations.NotNull;

import java.io.File;
import java.util.Collection;

/**
 * @author Eugene Petrenko (eugene.petrenko@jetbrains.com)
 *         09.12.10 12:44
 */
public interface RunnerSpec {
  @NotNull
  Collection<? extends ParameterDef> parameterDefs();

  @NotNull
  Collection<? extends RunnerStepSpec> runners();

  @NotNull
  String runType();

  @NotNull
  String shortName();

  @NotNull
  String description();

  @NotNull
  File getMetaRunnerRoot();
}
