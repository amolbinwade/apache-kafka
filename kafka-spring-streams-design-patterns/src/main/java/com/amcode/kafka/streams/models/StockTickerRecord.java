package com.amcode.kafka.streams.models;

import java.time.ZonedDateTime;
import java.util.Date;

public record StockTickerRecord(long eventDate, String stock, float stockPrice) {
}
