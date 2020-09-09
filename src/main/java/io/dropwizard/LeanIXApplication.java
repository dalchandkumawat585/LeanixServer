package io.dropwizard;

import io.dropwizard.Application;
import io.dropwizard.resources.TodoResource;
import io.dropwizard.setup.Bootstrap;
import io.dropwizard.setup.Environment;

public class LeanIXApplication extends Application<LeanIXConfiguration> {

    public static void main(final String[] args) throws Exception {
        new LeanIXApplication().run(args);
    }

    @Override
    public String getName() {
        return "LeanIX";
    }

    @Override
    public void initialize(final Bootstrap<LeanIXConfiguration> bootstrap) {
        // TODO: application initialization
    }

    @Override
    public void run(final LeanIXConfiguration configuration,
                    final Environment environment) {
        environment.jersey().register(TodoResource.class);
    }

}
