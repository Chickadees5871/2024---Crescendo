// Copyright (c) FIRST and other WPILib contributors.
// Open Source Software; you can modify and/or share it under the terms of
// the WPILib BSD license file in the root directory of this project.

package frc.robot;

import edu.wpi.first.wpilibj.Joystick;
import edu.wpi.first.wpilibj.XboxController;
import edu.wpi.first.wpilibj2.command.Command;
import edu.wpi.first.wpilibj2.command.Commands;
import edu.wpi.first.wpilibj2.command.InstantCommand;
import edu.wpi.first.wpilibj2.command.ParallelCommandGroup;
import edu.wpi.first.wpilibj2.command.SequentialCommandGroup;
import edu.wpi.first.wpilibj2.command.WaitCommand;
import edu.wpi.first.wpilibj2.command.button.JoystickButton;
import edu.wpi.first.wpilibj2.command.button.Trigger;
import frc.robot.commands.ArmGoToCommand;
import frc.robot.commands.DriveCommand;
import frc.robot.commands.TaxiCommand;
import frc.robot.subsystems.Arm;
import frc.robot.subsystems.Intake;
import frc.robot.subsystems.OperatorInterface;
import frc.robot.subsystems.Shooter;
import frc.robot.subsystems.SwerveDrive;
import frc.robot.subsystems.Hardware.ShooterHardware;

public class RobotContainer {
  public SwerveDrive swerveDrive;
  public OperatorInterface oi;
  public final DriveCommand driveCommand;
  public final Arm arm;
  public final Shooter shooter;
  public final Intake intake;

  public RobotContainer() {

    swerveDrive = new SwerveDrive();
    oi = new OperatorInterface();
    driveCommand = new DriveCommand(oi, swerveDrive);
    arm = Arm.getInstance();
    shooter = Shooter.getInstance();
    intake = Intake.getInstance();
    configureBindings();
    System.out.println(ShooterHardware.getInstance());
  }

  private void configureBindings() {
    Trigger shootAmp = new JoystickButton(oi.gunnerController, 5);
    Trigger shootSpeaker = new JoystickButton(oi.gunnerController, 6);
    Trigger speakerPos = new JoystickButton(oi.gunnerController, 4);
    Trigger carry = new JoystickButton(oi.gunnerController, 12);
    Trigger ampPos = new JoystickButton(oi.gunnerController, 3);
    Trigger intake2 = new JoystickButton(oi.driveController, XboxController.Button.kRightBumper.value);

    Trigger gyroReset = new JoystickButton(oi.driveController, XboxController.Button.kLeftBumper.value);


    carry.onTrue(new ArmGoToCommand(arm, 40));
    carry.onFalse(new ArmGoToCommand(arm, 4));

    intake2.onTrue(new ParallelCommandGroup(new InstantCommand(() -> {
      shooter.accept(0);
      intake.accept(-10);

    }), new ArmGoToCommand(arm,4)));

    intake2.onFalse(new ParallelCommandGroup(new InstantCommand(() -> {
      shooter.accept(0);
      intake.accept(0);
    }, shooter, intake), new ArmGoToCommand(arm, 4)));

    shootSpeaker.onTrue(new SequentialCommandGroup(
        new InstantCommand(() -> {
          shooter.accept(0);
          intake.accept(3);
        }, shooter, intake),
        new WaitCommand(.1),
        new InstantCommand(() -> {
          shooter.accept(-12);
          System.out.println("Shooter here");
          intake.accept(0);
        }, shooter, intake),

        new ArmGoToCommand(arm, 13),
        new WaitCommand(2),
        new InstantCommand(() -> {
          intake.accept(-12);
          System.out.println("Intake here");
        }, intake)));
        

    shootAmp.onTrue(new SequentialCommandGroup(
      new InstantCommand(() -> {
          shooter.accept(0);
          intake.accept(3);
        }, shooter, intake),
        new WaitCommand(.1),
        new InstantCommand(() -> {
          shooter.accept(-12);
          intake.accept(0);
        }, shooter, intake),
        new ArmGoToCommand(arm, 90),
        new WaitCommand(.3),
        new InstantCommand(() -> {
          intake.accept(-12);
        }, intake)));

    shootSpeaker.onFalse(new ParallelCommandGroup(new InstantCommand(() -> {
      shooter.accept(0);
      intake.accept(0);
    }, shooter, intake), new ArmGoToCommand(arm, 4)));

    shootAmp.onFalse(new ParallelCommandGroup(new InstantCommand(() -> {
      shooter.accept(0);
      intake.accept(0);
    }, shooter, intake), new ArmGoToCommand(arm, 4)));

    ampPos.onTrue(new ArmGoToCommand(arm, 90));
    speakerPos.onTrue(new ArmGoToCommand(arm, 12));

    gyroReset.onTrue( new InstantCommand( () -> { swerveDrive.resetGyro(); }));

  }  

  public Command getAutonomousCommand() {
    return new SequentialCommandGroup(
      new InstantCommand(() -> {
          shooter.accept(0);
          intake.accept(3);
        }, shooter, intake),
        new WaitCommand(.1),
        new InstantCommand(() -> {
          shooter.accept(-12);
          intake.accept(0);
        }, shooter, intake),
        new ArmGoToCommand(arm, 12),
        new WaitCommand(1.5),
        new InstantCommand(() -> {
          intake.accept(-12);
        }, intake)).andThen(new TaxiCommand(swerveDrive)).andThen(
            new ParallelCommandGroup(new InstantCommand(() -> {
              shooter.accept(0);
              intake.accept(0);
            }, shooter, intake), new ArmGoToCommand(arm, 4)));
  }
}