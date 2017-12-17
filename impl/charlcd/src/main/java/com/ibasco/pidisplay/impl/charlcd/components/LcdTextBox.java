package com.ibasco.pidisplay.impl.charlcd.components;

import com.ibasco.pidisplay.core.ui.CharGraphics;
import com.ibasco.pidisplay.core.ui.Graphics;
import com.ibasco.pidisplay.core.ui.components.DisplayTextBox;
import com.ibasco.pidisplay.core.util.GraphicsUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@SuppressWarnings("WeakerAccess")
public class LcdTextBox extends DisplayTextBox<CharGraphics> {

    private static final Logger log = LoggerFactory.getLogger(LcdTextBox.class);

    private LcdText content;

    public LcdTextBox() {
        this(null, null);
    }

    public LcdTextBox(Integer width, Integer height) {
        this(null, null, width, height);
    }

    public LcdTextBox(Integer left, Integer top, Integer width, Integer height) {
        super(left, top, width, height);
    }

    @Override
    protected int fixCaretPos(int pos) {
        int tmp = pos;
        if (tmp < 0)
            tmp = 0;
        int max = (getWidth() * getHeight()) - 1;
        if (tmp > max)
            tmp = max;
        return tmp;
    }

    @Override
    protected LcdText getContent() {
        if (this.content == null)
            this.content = new LcdText(getLeftPos(), getTopPos(), getWidth(), getHeight(), "");
        return this.content;
    }

    @Override
    protected int calcXOffset() {
        return getLeftPos() + GraphicsUtils.calcXOffset(getWidth(), this.caretPos.get());
    }

    @Override
    protected int calcYOffset() {
        return getTopPos() + GraphicsUtils.calcYOffset(getWidth(), getHeight(), this.caretPos.get());
    }

    @Override
    protected void updateCaretPos() {
        if (!isFocused())
            return;
        final Graphics graphics = this.getController().getGraphics();
        int startX = getLeftPos(), startY = getTopPos();
        int endX = startX + (getWidth() - 1);
        int endY = startY + (getHeight() - 1);
        int x = calcXOffset();
        int y = calcYOffset();
        if (x > endX)
            x = endX;
        if (y > endY)
            y = endY;
        log.debug("Updating Caret (X={}, Y={}, Node={}, Focused={})", x, y, this, isFocused());
        graphics.setCursor(x, y);
        graphics.setDisplayCursor(x, y);
    }

    @Override
    protected void drawNode(CharGraphics graphics) {
    }
}
