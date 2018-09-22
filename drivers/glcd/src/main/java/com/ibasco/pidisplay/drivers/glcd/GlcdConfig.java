package com.ibasco.pidisplay.drivers.glcd;

import com.ibasco.pidisplay.drivers.glcd.enums.GlcdBusInterface;
import com.ibasco.pidisplay.drivers.glcd.enums.GlcdRotation;
import com.ibasco.pidisplay.drivers.glcd.enums.GlcdSize;
import com.ibasco.pidisplay.drivers.glcd.exceptions.GlcdConfigException;
import com.ibasco.pidisplay.drivers.glcd.exceptions.GlcdException;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.builder.ReflectionToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;
import org.slf4j.Logger;

import java.util.Arrays;

import static org.slf4j.LoggerFactory.getLogger;

/**
 * Configuration class to be used by the glcd native library
 *
 * @author Rafael Ibasco
 */
public class GlcdConfig {

    public static final Logger log = getLogger(GlcdConfig.class);

    private GlcdDisplay display;
    private GlcdBusInterface busInterface;
    private GlcdRotation rotation;
    private GlcdPinMapConfig pinMap;
    private int deviceAddress = -1;
    private String setupProcedure;

    /**
     * @return The device address if available
     */
    public int getDeviceAddress() {
        return deviceAddress;
    }

    /**
     * Sets the device address of the display. This is typically used for I2C communication.
     *
     * @param deviceAddress
     *         The device address of the display
     */
    public void setDeviceAddress(int deviceAddress) {
        this.deviceAddress = deviceAddress;
    }

    public void setDisplay(GlcdDisplay display) {
        this.display = display;
    }

    public void setBusInterface(GlcdBusInterface busInterface) {
        this.busInterface = busInterface;
    }

    public void setRotation(GlcdRotation rotation) {
        this.rotation = rotation;
    }

    public void setPinMapConfig(GlcdPinMapConfig pinMap) {
        this.pinMap = pinMap;
    }

    public GlcdSize getDisplaySize() {
        return display.getDisplaySize();
    }

    public GlcdBusInterface getBusInterface() {
        return busInterface;
    }

    public GlcdPinMapConfig getPinMap() {
        return pinMap;
    }

    public GlcdDisplay getDisplay() {
        return display;
    }

    public GlcdRotation getRotation() {
        return rotation;
    }

    public String getSetupProcedure() {
        if (StringUtils.isBlank(setupProcedure)) {
            setupProcedure = lookupSetupInfo().getFunction();
        }
        return setupProcedure;
    }

    private GlcdSetupInfo lookupSetupInfo() {
        if (display == null) {
            throw new RuntimeException("Unable to obtain setup procedure",
                    new GlcdConfigException("Display has not been set", this)
            );
        }
        if (busInterface == null) {
            throw new RuntimeException("Unable to obtain setup procedure",
                    new GlcdConfigException("Protocol not set", this));
        }

        GlcdSetupInfo setupInfo = Arrays.stream(display.getSetupDetails())
                .filter(setup -> setup.isSupported(busInterface))
                .findFirst()
                .orElse(null);

        if (setupInfo == null)
            throw new RuntimeException("Unable to obtain setup procedure",
                    new GlcdException(String.format("Could not find a suitable setup procedure for bus interface '%s'", busInterface.name())));

        log.debug("Using display setup procedure (Display: {}, Protocol: {}, Setup Proc: {}))", display.getName(), busInterface.name(), setupInfo.getFunction());

        return setupInfo;
    }

    @Override
    public String toString() {
        return ReflectionToStringBuilder.toString(this, ToStringStyle.SIMPLE_STYLE);
    }
}
