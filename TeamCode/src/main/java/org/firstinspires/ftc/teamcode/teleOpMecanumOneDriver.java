package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.eventloop.opmode.Disabled;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.util.ElapsedTime;
import com.qualcomm.robotcore.util.Range;

import edu.spa.ftclib.internal.drivetrain.MecanumDrivetrain;
import edu.spa.ftclib.internal.state.ToggleBoolean;
import edu.spa.ftclib.internal.state.Button;

//@Disabled
@TeleOp(name="One Driver Mecanum", group="Mecanum Drivetrain")

public class teleOpMecanumOneDriver extends LinearOpMode {

    private ElapsedTime runtime = new ElapsedTime();
    private greaseBBQLightning robot = new greaseBBQLightning();
    //private Button RNPFixStart;
    private ToggleBoolean RNPFix;
    private ToggleBoolean oneStack;
    private ToggleBoolean twoStack;
    public MecanumDrivetrain drivetrain;
    // declare motor speed variables
    double RF; double LF; double RR; double LR;
    int pressCounter = 0;

    @Override
    public void runOpMode() {
        RNPFix = new ToggleBoolean();
        oneStack = new ToggleBoolean();
        twoStack = new ToggleBoolean();

        robot.init(hardwareMap);
        drivetrain = new MecanumDrivetrain(new DcMotor[]{robot.myBigMotorFrontLeft, robot.myBigMotorFrontRight, robot.myBigMotorBackLeft, robot.myBigMotorBackRight});

        robot.myBigMotorRandP.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        //robot.myBigMotorRandP.setMode(DcMotor.RunMode.RUN_USING_ENCODER);

        /**robot.myBigMotorBackLeft.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);
        robot.myBigMotorBackRight.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);
        robot.myBigMotorFrontLeft.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);
        robot.myBigMotorFrontRight.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);
        robot.myBigMotorLeftLifter.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);
        robot.myBigMotorRightLifter.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);*/




        // Wait for the game to start (driver presses PLAY)
        waitForStart();
        runtime.reset();

        while (opModeIsActive()) {
            //Run the holonomic formulas for each wheel
            //This is the easiest implementation for mecanum wheels
            LF = gamepad1.left_stick_y - gamepad1.left_stick_x - (gamepad1.right_stick_x);
            RF = gamepad1.left_stick_y + gamepad1.left_stick_x + (gamepad1.right_stick_x);
            LR = gamepad1.left_stick_y + gamepad1.left_stick_x - (gamepad1.right_stick_x);
            RR = gamepad1.left_stick_y - gamepad1.left_stick_x + (gamepad1.right_stick_x);

            robot.myBigMotorFrontLeft.setPower(Range.clip(LF, -1, 1) * 1);
            robot.myBigMotorFrontRight.setPower(Range.clip(RF, -1, 1) * 1);
            robot.myBigMotorBackLeft.setPower(Range.clip(LR, -1, 1) * 1);
            robot.myBigMotorBackRight.setPower(Range.clip(RR, -1, 1) * 1);

            ////////////////////////////////////////
            //Rack and Pinion Motor Control
            ///////////////////////////////////////

            ////////////////////////////////////////
            //Fix R and P when stuck in up position
            ///////////////////////////////////////


//
//            while(RNPFix.output()){
//                ToggleBoolean RNPFixStop = new ToggleBoolean();
//                RNPFixStop.input(this.gamepad1.dpad_right);
//
//                while(!RNPFixStop.output())
//                    robot.myBigMotorRandP.setPower(-.3);
//
//                robot.myBigMotorRandP.setPower(0);
//                robot.myBigMotorRandP.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
//                robot.myBigMotorRandP.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
//                break;
//            }


            ///////////////////////////////////
            //End Fix R and P when Stuck
            ///////////////////////////////////
           while (this.gamepad1.right_trigger > 0 && robot.myBigMotorRandP.getCurrentPosition() <= 3500 && opModeIsActive()) {
               robot.myBigMotorRandP.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
               if (robot.myBigMotorRandP.getCurrentPosition() >= 3500) {
                    robot.myBigMotorRandP.setPower(0);
                    break;
                }

                robot.myBigMotorRandP.setPower(this.gamepad1.right_trigger / 1.5);

            }


            while (this.gamepad1.left_trigger > 0 && robot.myBigMotorRandP.getCurrentPosition() >= 0 && opModeIsActive()) {
                robot.myBigMotorRandP.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
                if (robot.myBigMotorRandP.getCurrentPosition() <= 0) {
                    robot.myBigMotorRandP.setPower(0);
                    break;
                }

                robot.myBigMotorRandP.setPower(-this.gamepad1.left_trigger / 1.5);
            }

            //Set RNP to half height for stacking
            if(this.gamepad1.dpad_down)
            {
                robot.setRackAndPinionHeight(1750,1);
            }

            //Set RNP to full height for stacking
            if(this.gamepad1.dpad_up){
                robot.setRackAndPinionHeight(3500,1);
            }

            //Set RandP motor power to zero just as a safe guard when nothing is being pressed
            //if(!RNPFixStart.Press())
            //oneStack.input(this.gamepad1.dpad_down);
            //twoStack.input(this.gamepad1.dpad_up);

            if(!robot.RNPMoving) {
                robot.myBigMotorRandP.setMode(DcMotor.RunMode.RUN_TO_POSITION);
                robot.myBigMotorRandP.setPower(0);
            }


            ////////////////////////////////////
            //End Rack and Pinion Control
            ///////////////////////////////////

            ///////////////////////////////
            //Claw Control
            ///////////////////////////////
            //Claw Down
            //////////////////////////////
            if (this.gamepad1.right_bumper) {
                robot.myBigServoClaw.setPower(1);
            }

            //Claw up
            if (this.gamepad1.left_bumper) {
                robot.myBigServoClaw.setPower(-.1);
            }

            ///////////////////////////////
            //End Claw Control
            //////////////////////////////

            /////////////////////////////
            //Foundation Control
            ////////////////////////////
            if (this.gamepad1.y) {
                robot.myBigServoFoundation.setPower(-1);
            }

            if (this.gamepad1.x) {
                robot.myBigServoFoundation.setPower(0);
            }
            ///////////////////////////
            //End Foundation Control
            //////////////////////////

            telemetry.addData("RNP Motor Position", "%7d", robot.myBigMotorRandP.getCurrentPosition());
            telemetry.addData("RNP Motor Speed Up", this.gamepad1.right_trigger);
            telemetry.addData("RNP Motor Speed Down", this.gamepad1.left_trigger);
            telemetry.update();
        }
    }
}
