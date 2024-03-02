package frc.robot.subsystems;

import com.revrobotics.CANSparkMax;
import com.revrobotics.RelativeEncoder;
import com.revrobotics.SparkPIDController;
import com.revrobotics.CANSparkBase.ControlType;
import com.revrobotics.CANSparkBase.IdleMode;

import com.revrobotics.CANSparkLowLevel.MotorType;

import edu.wpi.first.math.controller.PIDController;
import edu.wpi.first.math.controller.ProfiledPIDController;
import edu.wpi.first.math.trajectory.TrapezoidProfile.Constraints;

public class Hardware {
    // define subsystem hardware here. Swerve is self contained so it is ignored
    public static class ArmHardware {
        private CANSparkMax leader;
        private CANSparkMax follower;
        private ProfiledPIDController pidController;
        private PIDController standardController;
        private RelativeEncoder encoder;
        private static final double kF = .1;
        private static ArmHardware instance;
        private SparkPIDController leadController;

        public static synchronized ArmHardware getInstance() {
            if (instance == null) {
                instance = new ArmHardware();
            }
            return instance;
        }

        private ArmHardware() {
            leader = new CANSparkMax(10, MotorType.kBrushless); 
            follower = new CANSparkMax(9, MotorType.kBrushless);
            leader.setInverted(true);

            encoder = leader.getEncoder();
            encoder.setPosition(0);

         encoder.setPositionConversionFactor(90 / (76.8)); // must be filled empirically
            leader.setIdleMode(IdleMode.kBrake);
            follower.setIdleMode(IdleMode.kBrake);
            // follower.setInverted(true);
            follower.follow(leader, true);
            // needs to be tuned empirically
            pidController = new ProfiledPIDController(.05, 0, 0,
                    new Constraints(60, 1000));

            standardController = new PIDController(.05, 0, 0);
        }

        private static double calculateFF(double theta) {
            return kF * Math.cos(theta);
        }

        public RelativeEncoder getEncoder(){
            return encoder;
        }

        // Takes in target angle, and drives towards target angle;
        public void execute(double position) {
            var output = pidController.calculate(encoder.getPosition(), position);
            leader.set(output);
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
            shooterMotor.set(voltage);
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
            intakeMotor.set(voltage);
        }
    }
}
