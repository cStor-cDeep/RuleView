# RuleView
尺子刻度 --   自定义view
###自定义view学习(第一章)

***
##### 1、自定义刻度尺控件
    
  在我们想要获取用户的身高体重等信息时，直接让他们输入显然不够友好偶然看到一款App用了类似刻度尺的界面让用户选择，觉得很赞。所有决定实现下。
   实现的最终效果如下图所示:
     
![7c0d4213-1242-496b-9898-0d04d14f3097.gif](https://github.com/panacena/RuleView/blob/master/7c0d4213-1242-496b-9898-0d04d14f3097.gif)

***
#####2、使用方式

######2.1 在gradle添加依赖
```
compile 'com.zkk.view:ZkkRulerView:1.0.0'
```
######2.2 在xml中设置

```
<com.zkk.view.rulerview.RulerView
	android:id="@+id/ruler_height"
	android:layout_width="fill_parent"
	android:layout_height="58.0dip"
	android:layout_marginTop="24.0dip"
	app:alphaEnable="true"
	app:lineColor="@color/gray"
	app:lineMaxHeight="40dp"
	app:lineMidHeight="30dp"
	app:lineMinHeight="20dp"
	app:lineSpaceWidth="10dp"
	app:lineWidth="2dip"
	app:textColor="@color/black"
	app:minValue="80.0"
	app:maxValue="250.0"
	app:perValue="1"
	app:selectorValue="165.0"
	/>
```
######2.3 在activity中只需调用一个方法和一个数值的回调
```
 ruler_height=(RulerView)findViewById(R.id.ruler_height);
 ruler_weight.setOnValueChangeListener(new RulerView.OnValueChangeListener() {
            @Override
            public void onValueChange(float value) {
                tv_register_info_weight_value.setText(value+"");
            }
        });

/**
     * 
     * @param selectorValue 未选择时 默认的值 滑动后表示当前中间指针正在指着的值
     * @param minValue   最大数值
     * @param maxValue   最小的数值
     * @param per   最小单位  如 1:表示 每2条刻度差为1.   0.1:表示 每2条刻度差为0.1 在demo中 身高mPerValue为1  体重mPerValue 为0.1
     */
ruler_weight.setValue(165, 80, 250, 1); 
```

***
##### 3、实现讲解
   
   博客地址：
 [http://www.jianshu.com/p/5d1fa50298b3](http://www.jianshu.com/p/5d1fa50298b3)


