package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.hardware.Gamepad;

@com.qualcomm.robotcore.eventloop.opmode.TeleOp(name = "Diff Swerve TeleOp", group = "TeleOp")
public class TeleOp extends OpMode {
    Robot robot;

    //deadband for joysticks
    public double DEADBAND_MAG = 0.1;
    public Vector2d DEADBAND_VEC = new Vector2d(DEADBAND_MAG, DEADBAND_MAG);

    public boolean willResetIMU = true;

    public void init() {
        robot = new Robot(this, false);
    }

    //allows driver to indicate that the IMU should not be reset
    //used when starting TeleOp after auto or if program crashes in the middle of match
    //relevant because of field-centric controls
    public void init_loop() {
        if (gamepad1.y) {
            willResetIMU = false;
        }
    }
    public void start () {
        if (willResetIMU) robot.resetYaw();
    }


    public boolean isFieldCentric = true;
    private boolean rightLast = false;

    public void loop() {
        // Zero encoders when D-Pad Up is pressed (align wheels forward first!)
        if (gamepad1.dpad_up) {
            robot.driveController.resetEncoders();
        }

        // Reset IMU heading when D-Pad Down is pressed
        if (gamepad1.dpad_down) {
            robot.resetYaw();
        }

        // Toggle Field Centric vs Robot Centric when D-Pad Right is pressed
        if (gamepad1.dpad_right && !rightLast) {
            isFieldCentric = !isFieldCentric;
        }
        rightLast = gamepad1.dpad_right;

        // Strafing is flipped, so we negate left_stick_x
        Vector2d joystick1 = new Vector2d(-gamepad1.left_stick_x, -gamepad1.left_stick_y); //LEFT joystick
        Vector2d joystick2 = new Vector2d(gamepad1.right_stick_x, -gamepad1.right_stick_y); //RIGHT joystick

        robot.driveController.updateUsingJoysticks(checkDeadband(joystick1), checkDeadband(joystick2), isFieldCentric);

        // Display module angles to verify zeroing
        telemetry.addData("Mode", isFieldCentric ? "FIELD CENTRIC" : "ROBOT CENTRIC");
        telemetry.addData("Module L Angle", robot.driveController.moduleLeft.getCurrentOrientation().getAngle());
        telemetry.addData("Module R Angle", robot.driveController.moduleRight.getCurrentOrientation().getAngle());
        
        telemetry.addData("Joystick 1", joystick1);
        telemetry.addData("Joystick 2", joystick2);
        telemetry.update();
    }

    //returns zero vector if joystick is within deadband
    public Vector2d checkDeadband(Vector2d joystick) {
        if (Math.abs(joystick.getX()) > DEADBAND_VEC.getX() || Math.abs(joystick.getY()) > DEADBAND_VEC.getY()) {
            return joystick;
        }
        return Vector2d.ZERO;
    }
}