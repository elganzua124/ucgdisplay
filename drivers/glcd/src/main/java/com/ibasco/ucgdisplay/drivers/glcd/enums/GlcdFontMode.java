/*-
 * ========================START=================================
 * UCGDisplay :: Graphics LCD driver
 * %%
 * Copyright (C) 2018 - 2020 Universal Character/Graphics display library
 * %%
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Lesser General Public License as
 * published by the Free Software Foundation, either version 3 of the
 * License, or (at your option) any later version.
 * 
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Lesser Public License for more details.
 * 
 * You should have received a copy of the GNU General Lesser Public
 * License along with this program.  If not, see
 * <http://www.gnu.org/licenses/lgpl-3.0.html>.
 * =========================END==================================
 */
package com.ibasco.ucgdisplay.drivers.glcd.enums;

/**
 * Enumeration that defines whether the glyph and string drawing functions will write the background color (solid)
 * or not (transparent).
 *
 * @author Rafael Ibasco
 */
public enum GlcdFontMode {
    /**
     * The glyph and string drawing functions will write the background color of the characters
     */
    SOLID(0),
    /**
     * The glyph and string drawing functions will NOT write the background color of the characters
     */
    TRANSPARENT(1);

    private int value;

    GlcdFontMode(int value) {
        this.value = value;
    }

    public int getValue() {
        return value;
    }
}
