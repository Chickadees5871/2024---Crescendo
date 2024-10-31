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
    // Initialize the subsystems
    swerveDrive = new SwerveDrive();
    oi = new OperatorInterface();
    driveCommand = new DriveCommand(oi, swerveDrive);
    arm = Arm.getInstance();
    shooter = Shooter.getInstance();
    intake = Intake.getInstance();

    // Sets Keybinds
    configureBindings();
    System.out.println(ShooterHardware.getInstance());
  }

  private void configureBindings() {
    // TODO: what are these bindings

    // Shooter Things ?
    Trigger shootAmp = new JoystickButton(oi.gunnerController, 5);
    Trigger shootSpeaker = new JoystickButton(oi.gunnerController, 6);

    // NOTE: Speaker and Amp is a game piece

    // I'm assuming this sets the pos of the arm to the joystick location.
    Trigger speakerPos = new JoystickButton(oi.gunnerController, 4);
    
    // No clue what these are
    Trigger carry = new JoystickButton(oi.gunnerController, 12);
    Trigger ampPos = new JoystickButton(oi.gunnerController, 3);
    Trigger intake2 = new JoystickButton(oi.driveController, XboxController.Button.kRightBumper.value);
    Trigger gyroReset = new JoystickButton(oi.driveController, XboxController.Button.kLeftBumper.value);

    // If this button is pressed/toggled set the arm to 40 Degrees, else set the arm angle to 4 Degrees
    carry.onTrue(new ArmGoToCommand(arm, 40)); 
    carry.onFalse(new ArmGoToCommand(arm, 4));

    // If the intake kebind is pressed/toggled the arm is set to 4 Degrees and the shooter accept motor stops and the intake motor starts going inwards
    intake2.onTrue(new ParallelCommandGroup(new InstantCommand(() -> {
      shooter.accept(0);
      intake.accept(-10);

    }), new ArmGoToCommand(arm,4)));

    // If the intake aint pressed/toggled the arm still goes down and the shooter still stops and the intake stops too
    intake2.onFalse(new ParallelCommandGroup(new InstantCommand(() -> {
      shooter.accept(0);
      intake.accept(0);
    }, shooter, intake), new ArmGoToCommand(arm, 4)));

    // If the shoot speaker button is pressed/toggled the shooter motor still stops and the intake goes outwards
    // Then waits 100ms then the shooter motor goes outwards and the intake stops. 
    // the the arm goes to 13 Deg the we wait 2 seconds then the intake motor goes inwards 
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
        
        // If the shoot amp button pressed/toggled then shooter motor stops and intake goes out then wait 100ms
        // Then the shooter goes out the the arm goes to 90 Deg the wait 300 ms
        // Then intake goes out
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

        // When not shooting stop the intake and the shooter motors then move armto 4 Deg
    shootSpeaker.onFalse(new ParallelCommandGroup(new InstantCommand(() -> {
      shooter.accept(0);
      intake.accept(0);
    }, shooter, intake), new ArmGoToCommand(arm, 4)));
    
    shootAmp.onFalse(new ParallelCommandGroup(new InstantCommand(() -> {
      shooter.accept(0);
      intake.accept(0);
    }, shooter, intake), new ArmGoToCommand(arm, 4)));

    // When amp pos pressed go to 90 Deg
    ampPos.onTrue(new ArmGoToCommand(arm, 90));
    // When speaker pos pressed arm goes to 12 Deg
    speakerPos.onTrue(new ArmGoToCommand(arm, 12));

    // When gyroreset pressed reset the swerve drive gyros
    gyroReset.onTrue( new InstantCommand( () -> { swerveDrive.resetGyro(); }));

  }  

  // Auto Code
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