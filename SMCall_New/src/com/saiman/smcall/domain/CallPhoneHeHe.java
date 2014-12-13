package com.saiman.smcall.domain;

public class CallPhoneHeHe {
	private CallPhoneYinPin callPhoneYinPin;
	private static CallPhoneHeHe he;

	private CallPhoneHeHe() {
		super();

	}

	public static CallPhoneHeHe getIXinCallPhoneHeHe() {
		if (he == null) {
			he = new CallPhoneHeHe();
		}
		return he;
	}

	public CallPhoneYinPin getCallPhoneYinPin() {
		return callPhoneYinPin;
	}

	public void setCallPhoneYinPin(CallPhoneYinPin callPhoneYinPin) {
		this.callPhoneYinPin = callPhoneYinPin;
	}
}
