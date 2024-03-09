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
            leader = new CANSparkMax(10, MotorType.kBrushless); // need to be filled
            follower = new CANSparkMax(9, MotorType.kBrushless); // need to be filled
            leader.setInverted(true);

            encoder = leader.getEncoder();
            encoder.setPosition(0);

         encoder.setPositionConversionFactor(90 / (76.8)); // must be filled empirically
        //    encoder.setInverted(true);
        //    System.out.println(temp);
        //    if(true)
        //    throw new RuntimeException();
            leader.setIdleMode(IdleMode.kBrake);
            follower.setIdleMode(IdleMode.kBrake);
            // follower.setInverted(true);
            follower.follow(leader, true);
            // needs to be tuned empirically
            pidController = new ProfiledPIDController(.05, 0, 0,
                    new Constraints(60, 1000));

            standardController = new PIDController(.05, 0, 0);
            // leadController = leader.getPIDController();
            // leadController.setP(.1);
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
            // var output = standardController.calculate(encoder.getPosition(), position);
            // output += calculateFF(position);
            // if (Math.abs(output) < .1) {
            leader.set(output);
            // }
            // else {
            // leader.set(Math.signum(output) * .1);
            // }
            // if (Math.random() > .9) {
            //     System.out.println("encoder: " + encoder.getPosition() + " position: " + position);
            // }
            // System.out.println(output);
            // follower.set(.5);
            // leader.set(.5);
            // follower.setVoltage(8);
            // follower.follow(leader);

            // leadController.setReference(leader.getEncoder().getPosition() + 1000,
            // ControlType.kPosition);

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
