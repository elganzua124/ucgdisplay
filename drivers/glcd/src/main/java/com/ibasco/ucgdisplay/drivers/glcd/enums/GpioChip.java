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

import com.ibasco.ucgdisplay.drivers.glcd.GlcdOptionValue;
import com.ibasco.ucgdisplay.drivers.glcd.GlcdUserDefinedOption;

/**
 * An enumeration representing a Gpio Chip Device
 *
 * @author Rafael Ibasco
 */
public enum GpioChip implements GlcdOptionValue<Integer>, GlcdUserDefinedOption<GpioChip, Integer> {
    CHIP_0(0),
    CHIP_1(1),
    OTHER(-1);

    private int chipNumber;

    GpioChip(int chipNumber) {
        this.chipNumber = chipNumber;
    }

    @Override
    public Integer toValue() {
        return chipNumber;
    }

    @Override
    public Class<Integer> getType() {
        return Integer.class;
    }

    @Override
    public GpioChip value(Integer value) {
        GpioChip c = OTHER;
        c.chipNumber = value;
        return c;
    }
}
