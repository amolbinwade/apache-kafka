package com.amcode.kafka.streams.models;

import java.time.ZonedDateTime;
import java.util.Date;

public record StockTickerRecord(ZonedDateTime eventDate, String stock, float stockPrice) {
}
