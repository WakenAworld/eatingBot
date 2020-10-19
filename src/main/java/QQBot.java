import kotlin.coroutines.CoroutineContext;
import net.mamoe.mirai.Bot;
import net.mamoe.mirai.BotFactoryJvm;
import net.mamoe.mirai.event.EventHandler;
import net.mamoe.mirai.event.Events;
import net.mamoe.mirai.event.ListeningStatus;
import net.mamoe.mirai.event.SimpleListenerHost;
import net.mamoe.mirai.message.GroupMessageEvent;
import net.mamoe.mirai.message.data.*;
import net.mamoe.mirai.utils.BotConfiguration;
import org.jetbrains.annotations.NotNull;

import java.io.File;
import java.util.ArrayList;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

public class QQBot {
    private static ArrayList<String> canteens = new ArrayList<>();
    static final ScheduledExecutorService goodNight = Executors.newScheduledThreadPool(5);

    public static void main(String[] args) {
        final long QQ = 512845044;
        final String PASSWORD = "";


        canteens.add("学二二楼");
        canteens.add("学二一楼");
        canteens.add("学五一楼");
        canteens.add("学三一楼");
        canteens.add("学三二楼");
        Bot bot = BotFactoryJvm.newBot(QQ,PASSWORD, new BotConfiguration(){
            {
                // "/opt/project/deviceinfo/deviceInfo.json"
                fileBasedDeviceInfo("/opt/project/deviceinfo/deviceInfo.json");
            }
        });

        bot.login();

//        bot.getFriends().forEach(friend -> System.out.println(friend.getId()+":"+friend.getNick()));
        Events.registerEvents(bot, new SimpleListenerHost() {
            @EventHandler
            public ListeningStatus onGroupMessage(GroupMessageEvent event){
                try {
                    goodNight.scheduleAtFixedRate(()->{
                        bot.getGroup(821909167)
                                .sendMessage(MessageUtils.newChain("晚安晖神")
                                        .plus(new At(bot
                                                .getGroup(821909167)
                                                .getMembers()
                                                .get(2551730844L))));
                    },4,24, TimeUnit.HOURS);
                } catch (Exception e){
                    e.printStackTrace();
                }

                String msgString = event.getMessage().contentToString();
                if (msgString.equals("nb") || msgString.contains("zyh")){
                    QuoteReply quoteReply = new QuoteReply(event.getSource());
                    event.getGroup().sendMessage(quoteReply.plus("不过zyh确实nb"));
                } else if (msgString.equals("课程表")){
                    File file = new File("classtable.png");
                    if (file.exists()){
                        final Image image = event.getGroup().uploadImage(new File("classtable.png"));
                        // 上传一个图片并得到 Image 类型的 Message
                        final String imageId = image.getImageId(); // 可以拿到 ID
                        //final Image fromId = MessageUtils.newImage(imageId); // ID 转换得到 Image
                        event.getGroup().sendMessage(MessageUtils.newChain("网安课表")
                                .plus(image)); // 发送图片
                    } else {
                        event.getGroup().sendMessage("课表不存在啊");
                    }
                } else if(msgString.equals("去哪吃")){
                    int canteen_id = getRandomIndex(5);
                    event.getGroup().sendMessage(MessageUtils.newChain(canteens.get(canteen_id))
                            .plus(new Face(Face.aixin)));
                } else if(msgString.equals("学二") || msgString.equals("学三") || msgString.equals("学五") || msgString.equals("学一")){
                    int floor = getRandomIndex(2);
                    if (floor == 0){
                        event.getGroup().sendMessage(MessageUtils.newChain("一楼")
                                .plus(new Face(Face.aixin)));
                    } else {
                        event.getGroup().sendMessage(MessageUtils.newChain("二楼")
                                .plus(new Face(Face.aixin)));
                    }
                } else if(msgString.equals("晚安浩浩！") && event.getSource().getSender().getId() == 1046481906L){
                    QuoteReply quoteReply = new QuoteReply(event.getSource());
                    event.getGroup().sendMessage(quoteReply.plus("晚安").plus(new Face(Face.aixin)));
                }

                return ListeningStatus.LISTENING;
            }

            @Override
            public void handleException(@NotNull CoroutineContext context, @NotNull Throwable exception) {
                throw new RuntimeException("在事件处理中发生异常",exception);
            }
        });

        bot.join();
    }

    private static int getRandomIndex(int range){
        return (int)(System.currentTimeMillis() % 10) % range;
    }
}
