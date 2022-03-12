package cn.maxpixel.simplog.test;

import cn.maxpixel.simplog.test.benchmark.AllocationBenchmark;
import org.openjdk.jmh.runner.RunnerException;
import org.openjdk.jmh.runner.options.Options;
import org.openjdk.jmh.runner.options.OptionsBuilder;

public class BenchmarkTest {
    public void test() throws RunnerException {
        Options options = new OptionsBuilder()
                .include(AllocationBenchmark.class.getSimpleName())
                .warmupIterations(3)
                .measurementIterations(5)
                .forks(4)
                .build();
//        new Runner(options).run();
    }
}