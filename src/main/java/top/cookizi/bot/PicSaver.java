//package top.cookizi.saver;
//
//import lombok.SneakyThrows;
//import net.mamoe.mirai.console.command.CommandManager;
//import net.mamoe.mirai.console.permission.Permission;
//import net.mamoe.mirai.console.permission.PermissionId;
//import net.mamoe.mirai.console.permission.PermissionService;
//import net.mamoe.mirai.console.plugin.jvm.JavaPlugin;
//import net.mamoe.mirai.console.plugin.jvm.JvmPluginDescriptionBuilder;
//import net.mamoe.mirai.utils.MiraiLogger;
////import top.cookizi.saver.cmd.FriendPicCmd;
//
//
//public class PicSaver extends JavaPlugin {
//    public MiraiLogger logger = getLogger();
//
//    public static final PicSaver INSTANCE = new PicSaver(); // 必须 public static, 必须名为 INSTANCE
//
//    public PicSaver() {
//        super(new JvmPluginDescriptionBuilder("top.cookizi.saver.PicSaver.pic-saver",
//                "0.1.0")
//                .author("cookizi")
//                .info("下载收到的图片")
//                .build()
//        );
//    }
//
//
//    @SneakyThrows
//    @Override
//    public void onEnable() {
//
//        logger.info("下图插件加载完成，开始注册命令");
//        Application.main(null);
////        boolean b = CommandManager.INSTANCE.registerCommand(FriendPicCmd.INSTANCE, false);
//
//
////        PermissionService.getInstance().register(new PermissionId("picSaver", "*"), "没啥好描述的", Permission.getRootPermission());
//
//
////        logger.info("下图插件注册完成，注册结果=" + b);
//    }
//
//}
