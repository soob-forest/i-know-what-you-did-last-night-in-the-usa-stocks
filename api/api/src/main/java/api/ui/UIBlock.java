package api.ui;

import java.util.List;
import java.util.Map;

public record UIBlock(String type, Map<String, Object> props, List<UIBlock> children) {
  public static UIBlock of(String type, Map<String, Object> props, List<UIBlock> children) {
    return new UIBlock(type, props, children);
  }
}

