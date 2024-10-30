package frc.robot.commands;

import edu.wpi.first.math.kinematics.ChassisSpeeds;
import edu.wpi.first.wpilibj.Timer;
import edu.wpi.first.wpilibj2.command.Command;
import frc.robot.subsystems.SwerveDrive;

public class TaxiCommand extends Command {
    private SwerveDrive swerveDrive;
    private double timestamp;

    public TaxiCommand(SwerveDrive swerveDrive) {
        addRequirements(swerveDrive);
        this.swerveDrive = swerveDrive;
    }

    @Override
    public void initialize() {
        swerveDrive.accept(new ChassisSpeeds(0, -.1, 0));
        timestamp = Timer.getFPGATimestamp();

    }

    @Override
    public void end(boolean interrupted) {
        swerveDrive.accept(new ChassisSpeeds(0, 0, 0));
    }

    @Override
    public boolean isFinished() {
        return Timer.getFPGATimestamp() - timestamp > 2;
    }
}
