package com.akanoo.client.atmosphere;

import org.atmosphere.gwt.client.AtmosphereGWTSerializer;
import org.atmosphere.gwt.client.SerialTypes;
import org.atmosphere.gwt.shared.SerialMode;

@SerialTypes(value = { String.class }, mode = SerialMode.PLAIN, pushMode = SerialMode.PLAIN)
public abstract class StringSerializer extends AtmosphereGWTSerializer {
}
