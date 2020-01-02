package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.eventloop.opmode.Disabled;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.util.ElapsedTime;
import com.qualcomm.robotcore.util.Range;

import edu.spa.ftclib.internal.drivetrain.MecanumDrivetrain;


//@Disabled
@TeleOp(name="One Driver Mecanum", group="Mecanum Drivetrain")

public class teleOpMecanumOneDriver extends LinearOpMode {

    private ElapsedTime runtime = new ElapsedTime();
    private greaseBBQLightning robot = new greaseBBQLightning();
    public MecanumDrivetrain drivetrain;

    @Override
    public void runOpMode() {
        robot.init(hardwareMap);
        drivetrain = new MecanumDrivetrain(new DcMotor[]{robot.myBigMotorFrontLeft, robot.myBigMotorFrontRight, robot.myBigMotorBackLeft, robot.myBigMotorBackRight});

        robot.myBigMotorRandP.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        robot.myBigMotorRandP.setMode(DcMotor.RunMode.RUN_USING_ENCODER);

        robot.myBigMotorBackLeft.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);
        robot.myBigMotorBackRight.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);
        robot.myBigMotorFrontLeft.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);
        robot.myBigMotorFrontRight.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);
        robot.myBigMotorLeftLifter.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);
        robot.myBigMotorRightLifter.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);




        // Wait for the game to start (driver presses PLAY)
        waitForStart();
        runtime.reset();

        while (opModeIsActive()) {
            //Run the holonomic formulas for each wheel
            //This is the easiest implementation for mecanum wheels
            final double lf = gamepad1.left_stick_y - gamepad1.left_stick_x - (gamepad1.right_stick_x * .7f);
            final double rf = gamepad1.left_stick_y + gamepad1.left_stick_x + (gamepad1.right_stick_x * .7f);
            final double lr = gamepad1.left_stick_y + gamepad1.left_stick_x - (gamepad1.right_stick_x * .7f);
            final double rr = gamepad1.left_stick_y - gamepad1.left_stick_x + (gamepad1.right_stick_x * .7f);

            robot.myBigMotorFrontLeft.setPower(Range.clip(lf, -1, 1) * .5);
            robot.myBigMotorFrontRight.setPower(Range.clip(rf, -1, 1) * 5);
            robot.myBigMotorBackLeft.setPower(Range.clip(lr, -1, 1) * 5);
            robot.myBigMotorBackRight.setPower(Range.clip(rr, -1, 1) * 5);

            //Rack and Pinion Motor Control

            //Leave the code below commented until fixed. It will be used to reset the RandP motor
            //if it ever gets stuck in the wrong position.
            /**Fix R and P when stuck in up position
             if(this.gamepad1.left_bumper){
             reset = true;
             }

             while(reset){
             robot.myBigMotorRandP.setPower(-this.gamepad1.left_trigger);
             }
             */

            while (this.gamepad1.right_trigger > 0 && robot.myBigMotorRandP.getCurrentPosition() <= 3500 && opModeIsActive()) {
                if (robot.myBigMotorRandP.getCurrentPosition() >= 3500) {
                    robot.myBigMotorRandP.setPower(0);
                    break;
                }

                robot.myBigMotorRandP.setPower(this.gamepad1.right_trigger / 2);

            }


            //Set RandP motor power to zero just as a safe guard when nothing is being pressed
            //robot.myBigMotorRandP.setPower(0);

            while (this.gamepad1.left_trigger > 0 && robot.myBigMotorRandP.getCurrentPosition() >= 0 && opModeIsActive()) {
                if (robot.myBigMotorRandP.getCurrentPosition() <= 0) {
                    robot.myBigMotorRandP.setPower(0);
                    break;
                }

                robot.myBigMotorRandP.setPower(-this.gamepad1.left_trigger / 2);
            }

            //Set RandP motor power to zero just as a safe guard when nothing is being pressed
            robot.myBigMotorRandP.setPower(0);

            /**
             //Turn on rear stone lifters
             if(this.gamepad1.a){
             double power = robot.myBigMotorLeftLifter.getPower();

             if(power < 1) {
             power = power + .1;
             robot.myBigMotorLeftLifter.setPower(power);
             robot.myBigMotorRightLifter.setPower(power);
             }
             }

             //Turn off rear lifters
             if(this.gamepad1.b){
             double power = robot.myBigMotorLeftLifter.getPower();

             if(power > 0) {
             power = power - .1;
             robot.myBigMotorLeftLifter.setPower(power);
             robot.myBigMotorRightLifter.setPower(power);
             }
             }
             */
            if (this.gamepad1.right_bumper) {
                robot.myBigServoClaw.setPower(-1);
            }

            if (this.gamepad1.left_bumper) {
                robot.myBigServoClaw.setPower(0);
            }

            if (this.gamepad1.y) {
                robot.myBigServoFoundation.setPower(-1);
            }

            if (this.gamepad1.x) {
                robot.myBigServoFoundation.setPower(1);
            }

            //telemetry.addData("Stone Intake Power", robot.myBigMotorLeftLifter.getPower());
            telemetry.addData("Rack and Pinion Motor Position", "%7d",
                    robot.myBigMotorRandP.getCurrentPosition());
            telemetry.update();
        }
    }
}
