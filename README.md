### Introduction

---
A universal character/graphics display library for ARM embedded devices based on Java. Provides drivers for character based lcd devices (Hitachi HD44780) and over 40+ graphic monochrome lcd devices (Powered by U8g2). 

### Features

---
##### Display drivers

######  Character LCD driver features
* Pure java implementation for Hitachi HD44780 driver powered by Pi4j (Will probably add JNI/native support if performance is an issue)
* Designed with flexibility in mind allowing different configurations for interfacing with your ARM device (e.g. GPIO expanders/I2C/SPI)
* No mandatory pin mapping configuration! You have the freedom to choose whatever device pins you want to use for your LCD device.

###### Graphic LCD driver features
* Over 46+ display controllers supported. Refer to the table below for the supported display controllers.
* Powered by U8g2. Basically, the graphics lcd driver is just a wrapper for the u8g2 library, so the available drawing operations should be equivalent to the ones found in U8g2. 
* Virtual mode support.  
* Event-driven UI framework (Currently under development) 

### Supported display controllers

---
###### Character LCD

* Hitachi HD44780
    
###### Graphic/Dot-Matrix LCD

* A2PRINTER, HX1230, IL3820, IST3020, KS0108, LC7981, LD7032, LS013B7DH03, LS027B7DH01, MAX7219, NT7534, PCD8544, PCF8812, RA8835, SBN1661, SED1330, SED1520, SH1106, SH1107, SH1108, SH1122, SSD1305, SSD1306, SSD1309, SSD1317, SSD1322, SSD1325, SSD1326, SSD1327, SSD1329, SSD1606, SSD1607, ST75256, ST7565, ST7567, ST7586S, ST7588, ST7920, T6963, UC1601, UC1604, UC1608, UC1610, UC1611, UC1638, UC1701
 
### Pre-requisites

---
* Wiring Pi library (Required by native module)
 
### Installation

---
* Note: Make sure you have Wiring Pi library installed on your ARM device

### Usage examples

---
######  Character LCD Example (HD44780)

```java
package com.ibasco.ucgdisplay.examples;

import com.ibasco.ucgdisplay.drivers.clcd.HD44780DisplayDriver;
import com.ibasco.ucgdisplay.drivers.clcd.LcdGpioAdapter;
import com.ibasco.ucgdisplay.drivers.clcd.LcdPinMapConfig;
import com.ibasco.ucgdisplay.drivers.clcd.LcdTemplates;
import com.ibasco.ucgdisplay.drivers.clcd.adapters.GpioLcdAdapter;
import com.ibasco.ucgdisplay.drivers.clcd.adapters.Mcp23017LcdAdapter;
import com.ibasco.ucgdisplay.drivers.clcd.adapters.ShiftRegisterLcdAdapter;
import com.ibasco.ucgdisplay.drivers.clcd.enums.LcdPin;
import com.ibasco.ucgdisplay.drivers.clcd.providers.MCP23017GpioProviderExt;
import com.pi4j.io.gpio.GpioFactory;
import com.pi4j.io.gpio.RaspiPin;
import com.pi4j.io.i2c.I2CBus;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class HD44780Example {

    private static final Logger log = LoggerFactory.getLogger(HD44780Example.class);

    public static void main(String[] args) {
        try {
            LcdPinMapConfig config = new LcdPinMapConfig()
                    .map(LcdPin.RS, RaspiPin.GPIO_02)
                    .map(LcdPin.EN, RaspiPin.GPIO_03)
                    .map(LcdPin.DATA_4, RaspiPin.GPIO_04)
                    .map(LcdPin.DATA_5, RaspiPin.GPIO_05)
                    .map(LcdPin.DATA_6, RaspiPin.GPIO_06)
                    .map(LcdPin.DATA_7, RaspiPin.GPIO_07);
            
            //GPIO adapter
            LcdGpioAdapter adapter = new GpioLcdAdapter(config);

            //Shift register adapter
            //LcdGpioAdapter adapter = new ShiftRegisterLcdAdapter(GpioFactory.getDefaultProvider(), RaspiPin.GPIO_02, RaspiPin.GPIO_03, RaspiPin.GPIO_04, config);

            //MCP23017 I2C adapter (Using built-in templates)
            //MCP23017GpioProviderExt mcp23017GpioProvider = new MCP23017GpioProviderExt(I2CBus.BUS_1, 0x15);
            //LcdGpioAdapter adapter = new Mcp23017LcdAdapter(mcp23017GpioProvider, LcdTemplates.ADAFRUIT_I2C_RGBLCD_MCP23017);
            
            HD44780DisplayDriver charDriver = new HD44780DisplayDriver(adapter, 20, 4);
            charDriver.home();
            charDriver.write("Hello World".getBytes());
        } catch (Exception e) {
            log.error("Error occured", e);
        }
    }
}

```

######  Graphic LCD Example (ST7920)

Simple hello world example for ST7920 controller

```java
package com.ibasco.ucgdisplay.examples;

import com.ibasco.ucgdisplay.drivers.glcd.*;
import com.ibasco.ucgdisplay.drivers.glcd.enums.GlcdBusInterface;
import com.ibasco.ucgdisplay.drivers.glcd.enums.GlcdFont;
import com.ibasco.ucgdisplay.drivers.glcd.enums.GlcdPin;
import com.ibasco.ucgdisplay.drivers.glcd.enums.GlcdRotation;

public class GlcdST7920Example {
    public static void main(String[] args) {
        GlcdConfig config = GlcdConfigBuilder.create()
                .rotation(GlcdRotation.ROTATION_NONE)
                .busInterface(GlcdBusInterface.SPI_HW_4WIRE_ST7920)
                .display(Glcd.ST7920.D_128x64)
                .pinMap(new GlcdPinMapConfig()
                        .map(GlcdPin.SPI_CLOCK, 0)
                        .map(GlcdPin.SPI_MOSI, 0)
                        .map(GlcdPin.CS, 0)
                )
                .build();

        GlcdDriver driver = new GlcdDriver(config);


        driver.setCursor(0, 10);
        driver.setFont(GlcdFont.FONT_6X12_MR);
        driver.clearBuffer();
        driver.drawString("Hello World");
        driver.sendBuffer();
    }
}

```

### Limitations

---
* This library is guaranteed to work on the Raspberry Pi, but I cannot guarantee that it would work on other ARM based devices (e.g. Asus Tinker Board etc) as I only have Raspberry Pi in my possession.
* Only full buffer mode is supported on the graphics display driver. Page buffer mode is not available. 
* Colored graphics lcd devices are not supported. This is because u8g2 only supports monochrome displays.  

### Known Issues/Troubleshooting

---
* Click [here](https://docs.google.com/spreadsheets/d/1WDh6J3zFE3j332CEIOvFzXhryOhoF7VAGc9Pf5vEo0s/edit?usp=sharing "Google spreadsheets") to check the current status of each graphics display controller

### Contribution guidelines

---
* Build instructions (TODO)

### Related projects

---
* GLCD Emulator/Simulator (Coming soon)