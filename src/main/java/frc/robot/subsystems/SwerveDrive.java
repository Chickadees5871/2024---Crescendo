package frc.robot.subsystems;

import com.ctre.phoenix6.hardware.CANcoder;
import com.kauailabs.navx.frc.AHRS;
import com.revrobotics.CANSparkMax;
import com.revrobotics.CANSparkLowLevel.MotorType;

import edu.wpi.first.math.geometry.Rotation2d;
import edu.wpi.first.math.geometry.Translation2d;
import edu.wpi.first.math.kinematics.ChassisSpeeds;
import edu.wpi.first.math.kinematics.SwerveDriveKinematics;
import edu.wpi.first.wpilibj.SerialPort.Port;
import edu.wpi.first.wpilibj2.command.SubsystemBase;

public class SwerveDrive extends SubsystemBase {
    private SwerveModule[] modules;
    private SwerveDriveKinematics kinematics;
    private AHRS navX;

    public SwerveDrive() {
        modules = new SwerveModule[4];
        modules[0] = new SwerveModule(
                new CANSparkMax(2, MotorType.kBrushless),
                new CANSparkMax(1, MotorType.kBrushless), new CANcoder(50));
        modules[1] = new SwerveModule(
                new CANSparkMax(4, MotorType.kBrushless),
                new CANSparkMax(3, MotorType.kBrushless), new CANcoder(51));
        modules[2] = new SwerveModule(
                new CANSparkMax(6, MotorType.kBrushless),
                new CANSparkMax(5, MotorType.kBrushless), new CANcoder(52));
        modules[3] = new SwerveModule(
                new CANSparkMax(8, MotorType.kBrushless),
                new CANSparkMax(7, MotorType.kBrushless), new CANcoder(53));
        kinematics = new SwerveDriveKinematics(
                new Translation2d(15 * 2.54 / 100, -15 * 2.54 / 100),
                new Translation2d(15 * 2.54 / 100, 15 * 2.54 / 100),
                new Translation2d(-15 * 2.54 / 100, 15 * 2.54 / 100),
                new Translation2d(-15 * 2.54 / 100, -15 * 2.54 / 100));
        navX = new AHRS(Port.kMXP);
    }

    public void accept(ChassisSpeeds fieldCentricChassisSpeeds) {
        var chassis = ChassisSpeeds.fromFieldRelativeSpeeds(fieldCentricChassisSpeeds, Rotation2d.fromDegrees(navX.getAngle()));
        var states =  kinematics.toSwerveModuleStates(chassis);
       for (int i = 0; i < 4; i++) {
        modules[i].acceptMotion(states[i]);
       }
    }
}
