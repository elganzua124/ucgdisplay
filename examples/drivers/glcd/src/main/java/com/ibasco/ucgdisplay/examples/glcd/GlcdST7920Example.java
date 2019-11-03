package com.ibasco.ucgdisplay.examples.glcd;/*-
 * ========================START=================================
 * Organization: Universal Character/Graphics display library
 * Project: UCGDisplay :: Graphics LCD driver examples
 * Filename: GlcdST7920Example.java
 *
 * ---------------------------------------------------------
 * %%
 * Copyright (C) 2018 - 2019 Universal Character/Graphics display library
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

import com.ibasco.ucgdisplay.drivers.glcd.*;
import com.ibasco.ucgdisplay.drivers.glcd.enums.*;
import com.ibasco.ucgdisplay.drivers.glcd.exceptions.GlcdDriverException;
import com.ibasco.ucgdisplay.drivers.glcd.exceptions.XBMDecodeException;
import com.ibasco.ucgdisplay.drivers.glcd.utils.XBMData;
import com.ibasco.ucgdisplay.drivers.glcd.utils.XBMUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.time.Duration;
import java.util.Objects;

/**
 * ST7920 Example
 */
public class GlcdST7920Example {

    private static final Logger log = LoggerFactory.getLogger(GlcdST7920Example.class);

    public static void main(String[] args) throws Exception {
        new GlcdST7920Example().run();
    }

    private void drawU8G2Logo(int offset, GlcdDriver driver) {
        //driver.clearBuffer();
        driver.setFontMode(1);

        driver.setFontDirection(0);
        driver.setFont(GlcdFont.FONT_INB16_MF); //u8g2_font_inb16_mf
        driver.drawString(offset, 22, "U");

        driver.setFontDirection(1);
        driver.setFont(GlcdFont.FONT_INB19_MN); //u8g2_font_inb19_mn
        driver.drawString(offset + 14, 8, "8");

        driver.setFontDirection(0);
        driver.setFont(GlcdFont.FONT_INB16_MF); //u8g2_font_inb16_mf
        driver.drawString(offset + 36, 22, "g");
        driver.drawString(offset + 48, 22, "2");

        driver.drawHLine(offset + 2, 25, 34);
        driver.drawHLine(offset + 3, 26, 34);
        driver.drawVLine(offset + 32, 22, 12);
        driver.drawVLine(offset + 33, 23, 12);
        //driver.sendBuffer();
    }

    private void run() throws Exception {
        //SPI HW 4-Wire config for ST7920
        //Note: Since we are using the Raspberry Pi SPI hardware features, no pin mapping is needed.

        //Pin Mapping (Main SPI Peripheral)
        // - MOSI = 10
        // - SCLK = 11
        // - CE1 = 7

        GlcdConfig config = GlcdConfigBuilder
                .create(Glcd.ST7920.D_128x64, GlcdBusInterface.SPI_HW_4WIRE_ST7920)
                .option(GlcdOption.ROTATION, GlcdRotation.ROTATION_180)
                .option(GlcdOption.PROVIDER, Provider.SYSTEM)
                //.option(GlcdOption.PIGPIO_MODE, PigpioMode.DAEMON)
                .option(GlcdOption.DEVICE_SPEED, 800000)
                .option(GlcdOption.SPI_CHANNEL, SpiChannel.CHANNEL_1)
                /*.option(GlcdOption.DEVICE_GPIO_PATH, "/dev/gpiochip0")
                .option(GlcdOption.DEVICE_SPI_PATH, "/dev/spidev0.0")
                .option(GlcdOption.DEVICE_I2C_PATH, "/dev/i2c-1")
                .option(GlcdOption.I2C_BUS, 1)
                .option(GlcdOption.SPI_PERIPHERAL, SpiPeripheral.MAIN)
                .option(GlcdOption.SPI_CHANNEL, SpiChannel.CHANNEL_1)
                .option(GlcdOption.SPI_MODE, SpiMode.MODE_0)
                .option(GlcdOption.SPI_BIT_ORDER, SpiBitOrder.MSB_FIRST)
                .option(GlcdOption.SPI_BITS_PER_WORD, 8)
                .option(GlcdOption.I2C_FLAGS, 0)
                .option(GlcdOption.SPI_FLAGS, 0)
                .option(GlcdOption.I2C_DEVICE_ADDRESS, 0)
                .option(GlcdOption.PIGPIO_ADDRESS, null)
                .option(GlcdOption.PIGPIO_PORT, null)*/
                .build();

        GlcdDriver driver = new GlcdDriver(config);

        //Set the Font (This is required for drawing strings)
        driver.setFont(GlcdFont.FONT_6X12_MR);

        //Get the maximum character height
        int maxHeight = driver.getMaxCharHeight();

        long startMillis = System.currentTimeMillis();

        log.debug("Starting display loop");

        XBMData xbmData = XBMUtils.decodeXbmFile(getClass().getResourceAsStream("/ironman.xbm"));

        int offset = 50;

        for (int i = 1000; i >= 0; i--) {
            //Clear the GLCD buffer
            driver.clearBuffer();

            if (offset >= 128) {
                offset = 0;
            }

            drawU8G2Logo(offset++, driver);

            driver.drawXBM(0, 0, 45, 64, Objects.requireNonNull(xbmData).getData());

            //Write Operations to the GLCD buffer
            driver.setFont(GlcdFont.FONT_6X12_MR);
            driver.drawString(55, maxHeight * 3, "ucgdisplay");
            driver.drawString(55, maxHeight * 4, "1.5.0-alpha");
            driver.drawString(100, maxHeight * 5, String.valueOf(i));

            //Send all buffered data to the display
            driver.sendBuffer();

            Thread.sleep(1);
        }

        //Clear the display
        driver.clearDisplay();
        long endTime = System.currentTimeMillis() - startMillis;

        log.info("Done in {} seconds", Duration.ofMillis(endTime).toSeconds());
    }
}
