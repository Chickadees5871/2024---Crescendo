package frc.robot.subsystems;

import com.revrobotics.CANSparkMax;
import com.revrobotics.RelativeEncoder;

import com.revrobotics.CANSparkBase.IdleMode;

import com.revrobotics.CANSparkLowLevel.MotorType;

import edu.wpi.first.math.controller.ProfiledPIDController;
import edu.wpi.first.math.trajectory.TrapezoidProfile.Constraints;

public class Hardware {
    // define subsystem hardware here. Swerve is self contained so it is ignored
    public static class ArmHardware {
        private CANSparkMax leader;
        private CANSparkMax follower;
        private ProfiledPIDController pidController;
        private RelativeEncoder encoder;
        private static final double kF = .1;
        private static ArmHardware instance;

        public static synchronized ArmHardware getInstance() {
            if (instance == null) {
                instance = new ArmHardware();
            }
            return instance;
        }

        private ArmHardware() {
            leader = new CANSparkMax(10, MotorType.kBrushless); // need to be filled
            follower = new CANSparkMax(9, MotorType.kBrushless); // need to be filled
            follower.setInverted(true);

            encoder = leader.getEncoder();
            encoder.setPositionConversionFactor(77.4 / 88. * 360); // must be filled empirically
            encoder.setPosition(90);
            leader.setIdleMode(IdleMode.kBrake);
            follower.setIdleMode(IdleMode.kBrake);
            // needs to be tuned empirically
            pidController = new ProfiledPIDController(.1, 0, 0,
                    new Constraints(1, 1));
        }

        private static double calculateFF(double theta) {
            return kF * Math.cos(theta);
        }

        // Takes in target angle, and drives towards target angle;
        public  void execute(double position) {
            // var output = pidController.calculate(encoder.getPosition(), position);
            // // output += calculateFF(position);
            // if (Math.abs(output) < .1) {
            //     leader.set(output);
            // } else {
            //     leader.set(Math.signum(output) * .1);
            // }
            // System.out.println(output);
            follower.set(.5);
            leader.set(.5);
            // follower.setVoltage(8);

        }
    }

    public static class ShooterHardware {
        private CANSparkMax shooterMotor;
        private static ShooterHardware instance;
        private ShooterHardware() {
            shooterMotor = new CANSparkMax(11, MotorType.kBrushed);
            shooterMotor.setIdleMode(IdleMode.kCoast);
        }

        public static ShooterHardware getInstance() {
            if (instance == null) {
                instance = new ShooterHardware();
            }
            return instance;
        }

        public void execute(double voltage) {
            shooterMotor.setVoltage(voltage);
        }
    }

    public static class IntakeHardware {
        private CANSparkMax intakeMotor;
        private static IntakeHardware instance;

        private IntakeHardware() {
            intakeMotor = new CANSparkMax(12, MotorType.kBrushed);
        }

        public static IntakeHardware getInstance() {
            if (instance == null) {
                instance = new IntakeHardware();
            }
            return instance;
        }

        public void execute(double voltage) {
            intakeMotor.setVoltage(voltage);
        }
    }
}
