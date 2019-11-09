/*-
 * ========================START=================================
 * Organization: Universal Character/Graphics display library
 * Project: UCGDisplay :: Native :: Graphics
 * Filename: UcgPigpioI2CProvider.cpp
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
#include "UcgPigpioI2CProvider.h"
#include <pigpio.h>
#include <sstream>

UcgPigpioI2CProvider::UcgPigpioI2CProvider(UcgIOProvider *provider) : UcgI2CProvider(provider) {}

UcgPigpioI2CProvider::~UcgPigpioI2CProvider() = default;;

int UcgPigpioI2CProvider::open(const std::shared_ptr<ucgd_t> &context) {
    if (context->tp_i2c_handle > -1)
        throw I2COpenException("Device is already open");

    printDebugInfo(context);

    int busNumber = context->getOptionInt(OPT_I2C_BUS, DEFAULT_I2C_BUS);
    int address = context->getOptionInt(OPT_I2C_ADDRESS);
    int flags = context->getOptionInt(OPT_I2C_FLAGS);

    context->tp_i2c_handle = i2cOpen(busNumber, address, flags);
    if (context->tp_i2c_handle < 0) {
        std::stringstream ss;
        ss << "Failed to open I2C device. Reason: " << _get_errmsg(context->tp_i2c_handle);
        throw I2COpenException(ss.str());
    }

    return context->tp_i2c_handle;
}

int UcgPigpioI2CProvider::write(const std::shared_ptr<ucgd_t> &context, unsigned short address, const uint8_t *buffer, unsigned short length) {
    int retval = i2cWriteDevice(context->tp_i2c_handle, (char *) buffer, length);
    if (retval < 0) {
        std::stringstream ss;
        ss << "Failed to write to I2C device. Reason: " << _get_errmsg(retval);
        throw I2CWriteException(ss.str());
    }
    return retval;
}

int UcgPigpioI2CProvider::close(const std::shared_ptr<ucgd_t> &context) {
    return _close(context);
}

UcgPigpioProvider *UcgPigpioI2CProvider::getProvider() {
    return dynamic_cast<UcgPigpioProvider *>(UcgProviderBase::getProvider());
}

int UcgPigpioI2CProvider::_close(const std::shared_ptr<ucgd_t> &context) {
    int retval = i2cClose(context->tp_i2c_handle);
    if (retval < 0) {
        std::stringstream ss;
        ss << "Failed to close I2C device. Reason: " << _get_errmsg(retval);
        throw I2CException(ss.str());
    }
    context->tp_i2c_handle = -1;
    return retval;
}

std::string UcgPigpioI2CProvider::_get_errmsg(int val) {
    std::string reason;
    switch (val) {
        case PI_BAD_I2C_BUS:
            reason = "Bad I2C Bus";
            break;
        case PI_BAD_I2C_ADDR:
            reason = "Bad I2C Address";
            break;
        case PI_BAD_FLAGS:
            reason = "Bad I2C open flags";
            break;
        case PI_NO_HANDLE:
            reason = "No handle available";
            break;
        case PI_I2C_OPEN_FAILED: {
            //int busNumber = getOptionValueInt(OPT_I2C_BUS, DEFAULT_I2C_BUS);
            //reason = std::string("Can't open I2C device: ") + std::to_string(busNumber);
            reason = std::string("Can't open I2C device");
            break;
        }
        case PI_BAD_HANDLE: {
            reason = "Unknown handle";
            break;
        }
        case PI_BAD_PARAM: {
            reason = "Bad i2c/spi/ser parameter";
            break;
        }
        case PI_I2C_WRITE_FAILED: {
            reason = "I2C write failed";
            break;
        }
        default: {
            reason = "Unknown error";
            break;
        }
    }
    return reason;
}


