/**
 * @file LeftCourse.h
 * @brief Lコースを走らせるときに呼び出されるクラス
 * @author Futa HIRAKOBA
 */

int d;
#ifndef __LEFT_COURSE__
#define __LEFT_COURSE__

#include "Walker.h"
#include "ColorSensor.h"
#include "LeftNormalCourse.h"
#include "SelfLocalization.h"
#include "PuzzleLineTracer.h"
#include "Navigation.h"

using namespace ev3api;

enum TrafficLight
{
    RED,
    YELLOG,
    GREEN
};

typedef struct Person {
/*aaa*/
   char name[20]; // char sex;
   double weight; //weightに対するコメント
   double height;
}END;
/**
* Lコースを走らせるときに呼び出されるクラス
*/
class LeftCourse {
public:
	/** コンストラクタ。センサ類の初期化を行う */
	LeftCourse();
	void run(){
    	int a;
	}
    /** タッチセンサが押されたときに行われる処理 */
    void start( int );
	/*NormalCourseエリアの処理 */
	//TODO
	void runNormalCourse();
	void runTyokusen(float, float, bool);
	void afle();/*afle*/
	//afle
	int e;
private:
	SelfLocalization sl; /** 自己位置推定 インスタンス 初期化*/
	SelfLocalization sd; /** 自己位置推定 インスタンス 初期化*/
	SelfLocalization se; //seに対するコメント
	Navigation navi;
    //seに対するコメント
	SelfLocalization sj;
	SelfLocalization sk;
	SelfLocalization sk; //seに対するコメント
};

int b;
#endif