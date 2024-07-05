//
//
//   Licensed under the Apache License, Version 2.0 (the "License");
//   you may not use this file except in compliance with the License.
//   You may obtain a copy of the License at
//
//     http://www.apache.org/licenses/LICENSE-2.0
//
//   Unless required by applicable law or agreed to in writing, software
//   distributed under the License is distributed on an "AS IS" BASIS,
//   WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
//   See the License for the specific language governing permissions and
//   limitations under the License.
//

package io.warp1.script.ext.jfiles;

import java.io.FileWriter;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;

import io.warp10.script.NamedWarpScriptFunction;
import io.warp10.script.WarpScriptException;
import io.warp10.script.WarpScriptStack;
import io.warp10.script.WarpScriptStackFunction;

public class JSTORE extends NamedWarpScriptFunction implements WarpScriptStackFunction {
  public JSTORE(String name) {
    super(name);
  }

  @Override
  public Object apply(WarpScriptStack stack) throws WarpScriptException {
    Object top = stack.pop();

    List<byte[]> keysvalues = new ArrayList<>();

    if (!(top instanceof List)) {
      throw new WarpScriptException(getName() + " expects a list of [ key value ] lists on top of the stack.");
    }

    List<Object> kv = null;

    if (2 == ((List<?>) top).size()) {
      if (!(((List<?>) top).get(0) instanceof List)) {
        kv = new ArrayList<>();
        kv.add(top);
      }
    } else {
      kv = (List<Object>) top;
    }

    for (Object o: kv) {
      if (!(o instanceof List)) {
        throw new WarpScriptException(getName() + " expects a list of [ key value ] lists on top of the stack.");
      }
      List<Object> oo = (List<Object>) o;

      if (2 != oo.size()) {
        throw new WarpScriptException(getName() + " expects a list of [ key value ] lists on top of the stack.");
      }

      Object key = oo.get(0);

      if (key instanceof String) {
        keysvalues.add(key.toString().getBytes(StandardCharsets.UTF_8));
      } else {
        throw new WarpScriptException(getName() + " expects a list of [ key value ] (STRING,STRING or BYTES) lists on top of the stack.");
      }

      Object value = oo.get(1);

      if (value instanceof byte[]) {
        keysvalues.add((byte[]) value);
      } else if (value instanceof String) {
        keysvalues.add(value.toString().getBytes(StandardCharsets.UTF_8));
      } else {
        throw new WarpScriptException(getName() + " expects a list of [ key value ] (STRING,STRING or BYTES) lists on top of the stack.");
      }
    }

    // File writing part
    try (FileWriter fileWriter = new FileWriter("output.txt", true)) {
      for (int i = 0; i < keysvalues.size(); i += 2) {
        String key = new String(keysvalues.get(i), StandardCharsets.UTF_8);
        String value = new String(keysvalues.get(i + 1), StandardCharsets.UTF_8);
        fileWriter.write(key + ":" + value + "\n");
      }
    } catch (IOException e) {
      throw new WarpScriptException(getName() + " encountered an IOException.", e);
    }

    return stack;
  }
}

