package com.amcode.kafka.streams.datamodels;

public enum StocksEnum {
    ITC("ITC","ITC"),
    INFY("INFOSYS","INFY"),
    SBI("SBI","SBI"),
    RELIANCE("RELIANCE","REL"),
    HDFCBANK("HDFCBANK","HDFCB"),
    AXISBANK("AXISBANK","AXS"),
    NESTLE("NESTLE","NST"),
    PAGE("PAGE","PGE"),
    TATAMOTORS("TATAMOTORS","TATAM"),
    MARUTI("MARUTI","MRT");


    public final String name;
    public final String code;

    private StocksEnum(String name, String code){
        this.name=name;
        this.code=code;
    }
}
