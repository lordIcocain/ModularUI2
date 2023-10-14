package com.cleanroommc.modularui.api.drawable;

import com.cleanroommc.modularui.widget.sizer.Box;

/**
 * A {@link IDrawable} with a fixed size.
 */
public interface IIcon extends IDrawable {

    /**
     * @return width of this icon or 0 if the width should be dynamic
     */
    int getWidth();

    /**
     * @return height of this icon or 0 of the height should be dynamic
     */
    int getHeight();

    /**
     * @return the margin of this icon. Only used if width or height is 0
     */
    Box getMargin();

    IIcon EMPTY_2PX = EMPTY.asIcon().height(2);
}
