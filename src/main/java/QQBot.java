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
import java.util.Random;

public class QQBot {
    private static ArrayList<String> canteens = new ArrayList<>();

    public static void main(String[] args) {
        final long QQ = ;
        final String PASSWORD = "";
        canteens.add("学二二楼");
        canteens.add("学二一楼");
        canteens.add("学五一楼");
        canteens.add("学三一楼");
        canteens.add("学三二楼");
        Bot bot = BotFactoryJvm.newBot(QQ,PASSWORD, new BotConfiguration(){
            {
                fileBasedDeviceInfo("/opt/project/deviceinfo/deviceInfo.json");
            }
        });

        bot.login();

//        bot.getFriends().forEach(friend -> System.out.println(friend.getId()+":"+friend.getNick()));
        Events.registerEvents(bot, new SimpleListenerHost() {
            @EventHandler
            public ListeningStatus onGroupMessage(GroupMessageEvent event){
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
                    int canteen_id = (int)(System.currentTimeMillis() % 10) % 5;
                    event.getGroup().sendMessage(MessageUtils.newChain(canteens.get(canteen_id))
                            .plus(new Face(Face.se)));
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
}