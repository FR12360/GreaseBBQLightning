package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.eventloop.opmode.Disabled;
import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.util.ElapsedTime;
import com.qualcomm.robotcore.util.Range;

import edu.spa.ftclib.internal.drivetrain.MecanumDrivetrain;

/**
 * Created by Michaela on 1/3/2018.
 */

//@Disabled
@TeleOp(name="One Driver Mecanum", group="Mecanum Drivetrain")

public class teleOpMecanumOneDriver extends OpMode {

    private greaseBBQLightning robot = new greaseBBQLightning();
    private ElapsedTime runtime = new ElapsedTime();

    public MecanumDrivetrain drivetrain;

    private boolean reset = false;

    /**
     * User defined init method
     * <p>
     * This method will be called once when the INIT button is pressed.
     */
    @Override
    public void init() {
        robot.init(hardwareMap);
        drivetrain = new MecanumDrivetrain(new DcMotor[]{robot.myBigMotorFrontLeft, robot.myBigMotorFrontRight, robot.myBigMotorBackLeft, robot.myBigMotorBackRight});

        ////robot.myBigMotorFoundation.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        ////robot.myBigMotorRandP.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);

        ////robot.myBigMotorFoundation.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
        ////robot.myBigMotorRandP.setMode(DcMotor.RunMode.RUN_USING_ENCODER);

        robot.myBigServoFoundationMover.setPower(0);

        // Send telemetry message to indicate successful Encoder reset
//        telemetry.addData("Starting position Foundation & RandP",  "%7d: %7d",
//                robot.myBigMotorFoundation.getCurrentPosition(),
//                robot.myBigMotorRandP.getCurrentPosition());
//        telemetry.update();
    }

    /**
     * User defined loop method
     * <p>
     * This method will be called repeatedly in a loop while this op mode is running
     */
    @Override
    public void loop() {

        //Run the holonomic formulas for each wheel
        final double lf = gamepad1.left_stick_y - gamepad1.left_stick_x - (gamepad1.right_stick_x * .7f);
        final double rf = gamepad1.left_stick_y + gamepad1.left_stick_x + (gamepad1.right_stick_x * .7f);
        final double lr = gamepad1.left_stick_y + gamepad1.left_stick_x - (gamepad1.right_stick_x * .7f);
        final double rr = gamepad1.left_stick_y - gamepad1.left_stick_x + (gamepad1.right_stick_x * .7f);

        robot.myBigMotorFrontLeft.setPower(Range.clip(lf, -1, 1));
        robot.myBigMotorFrontRight.setPower(Range.clip(rf, -1, 1));
        robot.myBigMotorBackLeft.setPower(Range.clip(lr, -1, 1));
        robot.myBigMotorBackRight.setPower(Range.clip(rr, -1, 1));

        //Rack and Pinion Motor Control
/**
        //Fix R and P when stuck in up position
        if(this.gamepad1.left_bumper){
            reset = true;
        }

        while(reset){
            robot.myBigMotorRandP.setPower(-this.gamepad1.left_trigger);
        }

        while(this.gamepad1.right_trigger > 0 && robot.myBigMotorRandP.getCurrentPosition() <= 4400){
            if(robot.myBigMotorRandP.getCurrentPosition() >= 4400){
                robot.myBigMotorRandP.setPower(0);
                break;
            }

            robot.myBigMotorRandP.setPower(this.gamepad1.right_trigger);
        }

        robot.myBigMotorRandP.setPower(0);

        while(this.gamepad1.left_trigger > 0 && robot.myBigMotorRandP.getCurrentPosition() >= 0){
            if(robot.myBigMotorRandP.getCurrentPosition() <= 0) {
                robot.myBigMotorRandP.setPower(0);
                break;
            }

            robot.myBigMotorRandP.setPower(-this.gamepad1.left_trigger);
        }

        robot.myBigMotorRandP.setPower(0);

        //just used to move shaft
        //robot.myBigMotorFoundation.setPower(this.gamepad1.right_trigger);

        //Drop foundation claw

        if(this.gamepad1.b){
            //robot.myBigServoFoundationMover.setPower(0);
            robot.myBigMotorFoundation.setPower(0);
        }

        //Raise foundation claw
        if(this.gamepad1.y){
            //robot.myBigServoFoundationMover.setPower(1);
            robot.myBigMotorFoundation.setPower(.5);
        }

        //Store foundation claw
        if(this.gamepad1.a){
            //robot.myBigServoFoundationMover.setPower(-1);
            robot.myBigMotorFoundation.setPower(.25);
        }
*/
//        telemetry.addData("course", course);
//        telemetry.addData("velocity", velocity);
//        telemetry.addData("rotation", rotation);
//        telemetry.addData("Foundation & RandP Motor Position",  "%7d: %7d",
//                robot.myBigMotorFoundation.getCurrentPosition(),
//                robot.myBigMotorRandP.getCurrentPosition());
        ////telemetry.addData("Foundation",  "%7d",
        ////        robot.myBigMotorFoundation.getCurrentPosition());
       //// telemetry.addData("Left and Right Trigger",  "%15.8f: %15.8f",

        ////telemetry.update();
    }
}

