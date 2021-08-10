# DropEntityItem

基于Nukkit的击杀生物掉落生物插件


## 引言:
此插件安装后当玩家击杀生物时会掉落一个生物物品 可通过监听事件实现各种功能

#### 关键字: 事件 接口
#### 简介
#### 事件  
```
/**
  * 掉落物生成事件
  */
EntityDropItemSpawnEvent
/**
  * 掉落物移除事件
  */
EntityItemCloseEvent
/**
  * 掉落物附近有玩家拾取事件
  */
PlayerPickUpEntityEvent
```
 
#### 接口

```java
/**
  * 生成掉落物
  * @param e 坐标
  * @param entityId 掉落物的生物id
  **/
DropEntityItem.dropEntityItem(Position e,int entityId);
/**
  * 生成掉落物
  * @param entity 直接掉落抽象方法 AbstractDropEntity 生物
  **/
DropEntityItem.dropEntityItem(AbstractDropEntity entity);
```

 
