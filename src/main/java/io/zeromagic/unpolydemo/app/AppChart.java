package io.zeromagic.unpolydemo.app;

import java.time.Instant;
import java.time.ZoneOffset;
import java.time.ZonedDateTime;
import java.time.temporal.ChronoUnit;
import java.util.Arrays;
import java.util.List;
import java.util.Random;
import java.util.concurrent.ThreadLocalRandom;
import java.util.stream.DoubleStream;
import java.util.stream.Stream;

public record AppChart(List<Instant> timestamps, List<Series> series) {
  public record Series(String name, Purpose purpose, int[] data) {}

  public static AppChart requestsChart() {
    var timestamps = generateTimestamps();
    var allRequests = generateInts(timestamps.size(), 0, 10000, 300);
    var errorRequests = generateInts(timestamps.size(), 0, 1000, 100);
    // errorRequests cannot be greater than allRequests
    for (int i = 0; i < timestamps.size(); i++) {
      errorRequests[i] = Math.min(errorRequests[i], allRequests[i]);
    }
    return new AppChart(timestamps, List.of(
            new Series("OK", Purpose.INFO, allRequests),
            new Series("Error", Purpose.WARNING, errorRequests)
    ));
  }

  public static AppChart logsChart() {
    var timestamps = generateTimestamps();
    var allLogs = generateInts(timestamps.size(), 0, 1000, 50);
    var errorLogs = generateInts(timestamps.size(), 0, 500, 20);
    // errorLogs cannot be greater than allLogs
    for (int i = 0; i < timestamps.size(); i++) {
      errorLogs[i] = Math.min(errorLogs[i], allLogs[i]);
    }
    return new AppChart(timestamps, List.of(
            new Series("All", Purpose.INFO, allLogs),
            new Series("Error", Purpose.WARNING, errorLogs)
    ));
  }

  public static AppChart cpuAndMemoryChart() {
    var timestamps = generateTimestamps();
    var memory = generateInts(timestamps.size(), 0, 1000, 50);
    var cpu = generateInts(timestamps.size(), 0, 500, 20);
    return new AppChart(timestamps, List.of(
            new Series("Memory", Purpose.INFO_SECONDARY, memory),
            new Series("CPU", Purpose.INFO, cpu)
    ));
  }

  private static List<Instant> generateTimestamps() {
    var start = ZonedDateTime.now(ZoneOffset.UTC).truncatedTo(ChronoUnit.MINUTES);
    start = start.plusMinutes(15 - start.getMinute() % 15);
    return Stream.iterate(start, time -> time.minusMinutes(15))
            .limit(48)
            .map(ZonedDateTime::toInstant)
            .toList();
  }

  private static int[] generateInts(int count, int min, int max, int speed) {
    return gaussianWalk(count, min, max, speed)
            .mapToInt(value -> (int)value)
            .toArray();
  }

  private static DoubleStream gaussianWalk(int count, double min, double max, double speed) {
    var random = ThreadLocalRandom.current();
    return DoubleStream.iterate(
                    random.nextDouble(max - min) + min,
                    value -> Math.clamp(value + random.nextGaussian() * speed, min, max))
            .limit(count);
  }

  public enum Purpose {
    INFO, WARNING, INFO_SECONDARY
  }
}
