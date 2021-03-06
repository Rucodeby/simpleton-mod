package thesimpleton.ui.buttons;

import org.apache.logging.log4j.Logger;
import thesimpleton.TheSimpletonMod;
import thesimpleton.ui.ReadyButtonPanel;

public class ReadyButton extends CustomButton {
  private Logger logger = TheSimpletonMod.logger;

  public final static String BUTTON_ID = "TheSimpletonMod:ReadyButton";
  public final static String BUTTON_IMG = "ready-button-1";

  public final ReadyButtonPanel panel;

  public ReadyButton(float x, float y, float scale, String label, ReadyButtonPanel panel) {
    super(x, y, scale, BUTTON_IMG, label);
    this.panel = panel;
    logger.debug("ReadyButton instantiated");
  }

  @Override
  public void onClick() {
    logger.debug("ReadyButton.onClick Called");
    panel.onReadyClicked();
  }
}