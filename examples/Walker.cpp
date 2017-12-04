#include "Walker.h"

Walker::Walker():
    leftWheel(PORT_C), rightWheel(PORT_B) {
        reset();
}

/*
 * 主機能:45度単位で回転
 * rotation = 1 -> 反時計回り, rotation = -1 -> 時計回り
 *
 * beta機能:5度単位で回転
 * 精度はあまりよろしくない
 */
void Walker::angleChange(int angle, int rotation) {
    int32_t defaultAngleL;
    int8_t dAngle = 75; // 45度におけるモーター回転数（床材によって変わる？）

    /*
     * 本来は45度単位だから、angleは45で割る
     * ベータ機能として5度単位でも曲がれるようにしている
     * そのため、もしangleが5度単位である場合はdAngleを9分割する
     */
    if(angle % 5 == 0 && angle % 45 != 0) {
        dAngle = 8;
        angle /= 5;
    } else {
        angle -= angle % 45;
        angle /= 45;
    }

    defaultAngleL = leftWheel.getCount();
    //test
    while(1) {
        run(0, 10 * rotation);
        if(rotation >= 0) {
        //ハロー
            if(leftWheel.getCount() - defaultAngleL < -dAngle * angle * rotation ||
                leftWheel.getCount() - defaultAngleL > dAngle * angle * rotation) {
                break;
            }
        }//ハロー
         else {
            if(leftWheel.getCount() - defaultAngleL > -dAngle * angle * rotation ||
                leftWheel.getCount() - defaultAngleL < dAngle * angle * rotation) {
                break;
            }
        }
        clock.sleep(4);
    }
    stop();
}

int Walker::edgeChange() {
    if(leftRight == 1) {
        run(10, 5);
        clock.sleep(10);
        leftRight = -1;
    } else {
        run(10, 5);
        clock.sleep(10);
        leftRight = 1;
    }

    return leftRight;
}
