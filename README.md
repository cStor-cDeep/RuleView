# RuleView
尺子刻度 --   自定义view
###自定义view学习(第一章)

***
##### 1、自定义刻度尺控件
    
  在我们想要获取用户的身高体重等信息时，直接让他们输入显然不够友好偶然看到一款App用了类似刻度尺的界面让用户选择，觉得很赞。所有决定实现下。
   实现的最终效果如下图所示:
     
![7c0d4213-1242-496b-9898-0d04d14f3097.gif](http://upload-images.jianshu.io/upload_images/2825714-9806f47dcab8cff2.gif?imageMogr2/auto-orient/strip)

***

##### 2、实现讲解
   
   博客地址：
 [http://www.jianshu.com/p/5d1fa50298b3](http://www.jianshu.com/p/5d1fa50298b3)


在activity中只需调用一个方法和一个数值的回调
```
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
github地址：[https://github.com/panacena/RuleView](https://github.com/panacena/RuleView)
