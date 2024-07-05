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

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import io.warp10.script.NamedWarpScriptFunction;
import io.warp10.script.WarpScriptException;
import io.warp10.script.WarpScriptStack;
import io.warp10.script.WarpScriptStackFunction;

public class JLOAD extends NamedWarpScriptFunction implements WarpScriptStackFunction {
  public JLOAD(String name) {
    super(name);
  }
  
  @Override
  public Object apply(WarpScriptStack stack) throws WarpScriptException {
    
    Object top = stack.pop();
    
    List<String> keys = new ArrayList<>();
    
    boolean keylist = false;
    
    if (top instanceof String) {
      keys.add((String) top);
    } else if (top instanceof List) {
      for (Object k: (List<?>) top) {
        if (k instanceof String) {
          keys.add((String) k);
        } else {
          throw new WarpScriptException(getName() + " expects a key (STRING) or a list thereof on top of the stack.");          
        }
      }
      keylist = true;
    } else { 
      throw new WarpScriptException(getName() + " expects a key (STRING) or a list thereof on top of the stack.");
    }
    
    Map<String, String> data = new HashMap<>();

    try (BufferedReader br = new BufferedReader(new FileReader("output.txt"))) {
      String line;
      while ((line = br.readLine()) != null) {
        String[] parts = line.split(":", 2);
        if (parts.length == 2) {
          data.put(parts[0], parts[1]);
        }
      }
    } catch (IOException e) {
      throw new WarpScriptException(getName() + " encountered an IOException.", e);
    }
    
    if (keylist) {
      Map<String, String> results = new HashMap<>();
      for (String key : keys) {
        results.put(key, data.get(key));
      }
      stack.push(results);
    } else {
      stack.push(data.get(keys.get(0)));
    }

    return stack;
  }
}
