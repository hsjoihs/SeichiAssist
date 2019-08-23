package com.github.unchama.seichiassist.task;

import com.github.unchama.seichiassist.SeichiAssist;
import com.github.unchama.seichiassist.data.PlayerData;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.HashMap;
import java.util.UUID;

/**
 * プレイヤーデータ読み込み完了を確認したあと、一度だけ後の処理が実行される
 * runTaskTimerで実行されることを想定
 * ログイン後に同期処理が必要な場合はここに記述
 * @author unchama
 *
 */
public class PlayerDataUpdateOnJoin extends BukkitRunnable{

	private HashMap<UUID,PlayerData> playermap = SeichiAssist.Companion.getPlayermap();

	String name;
	Player p;
	final UUID uuid;
	final String struuid;
	int i;

	public PlayerDataUpdateOnJoin(PlayerData playerData) {
		name = playerData.getLowercaseName();
		uuid = playerData.getUuid();
		p = Bukkit.getPlayer(uuid);
		struuid = uuid.toString().toLowerCase();
		i = 0;
	}

	@Override
	public void run() {

		//プレイヤーデータ取得
		PlayerData playerdata = playermap.get(uuid);
		//念のためエラー分岐
		if(playerdata == null){
			if(i >= 9){
	 			//諦める
				p.sendMessage(ChatColor.RED + "初回ロードに失敗しています。再接続をお願いします。改善されない場合はお手数ですがお問い合わせください");
				p.kickPlayer(ChatColor.RED + "初回ロードに失敗しています。再接続をお願いします。改善されない場合はお手数ですがお問い合わせください");
	 			cancel();
	 			return;
	 		}else{
	 			//再試行
	 			p.sendMessage(ChatColor.YELLOW + "しばらくお待ちください…");
	 			i++;
	 			return;
	 		}
		}

		cancel();

		//同期処理をしないといけない部分ここから

		p.sendMessage(ChatColor.GREEN + "プレイヤーデータ取得完了");
		//join時とonenable時、プレイヤーデータを最新の状態に更新
		playerdata.updateOnJoin();

		//同期処理をしないといけない部分ここまで
	}

}
