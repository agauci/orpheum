package com.orpheum.orchestrator.backstage;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.scheduling.annotation.EnableScheduling;


@SpringBootApplication
@EnableConfigurationProperties
@EnableScheduling
public class BackstageApplication {

	public static void main(String[] args) {
		SpringApplication.run(BackstageApplication.class, args);
	}

//	private static void initializeOpenTelemetry() {
//		OpenTelemetrySdk sdk =
//				OpenTelemetrySdk.builder()
//						.setTracerProvider(SdkTracerProvider.builder().setSampler(Sampler.alwaysOn()).build())
//						.setLoggerProvider(
//								SdkLoggerProvider.builder()
//										.setResource(
//												Resource.getDefault().toBuilder()
//														.put(SERVICE_NAME, "orpheum-backstage")
//														.build())
//										.addLogRecordProcessor(
//												BatchLogRecordProcessor.builder(
//																OtlpGrpcLogRecordExporter.builder()
//																		.setEndpoint("http://localhost:4317")
//																		.build())
//														.build())
//										.build())
//						.build();
//
//		// Add hook to close SDK, which flushes logs
//		Runtime.getRuntime().addShutdownHook(new Thread(sdk::close));
//
//		// Register sdk with logging appender
//		OpenTelemetryAppender.install(sdk);
//	}

}
