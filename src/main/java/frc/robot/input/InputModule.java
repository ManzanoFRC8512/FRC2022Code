package frc.robot.input;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import javax.inject.Qualifier;

import com.google.inject.AbstractModule;

import edu.wpi.first.wpilibj.XboxController;

public final class InputModule extends AbstractModule {
    /** Annotates the Xbox controller which is used by the driver. */
    @Retention(RetentionPolicy.RUNTIME)
    @Qualifier
    public @interface XboxDriverController { }

    /** Annotates the Xbox controller which is used by the arm. */
    @Retention(RetentionPolicy.RUNTIME)
    @Qualifier
    public @interface XboxArmController { }

    @Override
    protected void configure() {
        bind(XboxController.class)
          .annotatedWith(XboxDriverController.class)
          .toInstance(new XboxController(0));

        bind(XboxController.class)
        .annotatedWith(XboxArmController.class)
        .toInstance(new XboxController(1));
    }
}
