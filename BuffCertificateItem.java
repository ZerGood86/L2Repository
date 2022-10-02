package handler.items;

import ru.j2dev.gameserver.Config;
import ru.j2dev.gameserver.data.xml.holder.ItemTemplateHolder;
import ru.j2dev.gameserver.model.Playable;
import ru.j2dev.gameserver.model.Player;
import ru.j2dev.gameserver.model.items.ItemInstance;
import ru.j2dev.gameserver.network.lineage2.serverpackets.InventoryUpdate;
import ru.j2dev.gameserver.network.lineage2.serverpackets.MagicSkillUse;
import ru.j2dev.gameserver.network.lineage2.serverpackets.SystemMessage;
import ru.j2dev.gameserver.utils.ItemFunctions;
import ru.j2dev.gameserver.utils.Merchant;

/**
 * @author : Alice
 * @date : 12.08.2022
 * @time : 17:23
 */
public class BuffCertificateItem extends ScriptItemHandler {
    private static final int[] itemIdList = { Config.BUFF_CERTIFICATE_ID };

    private static final int[][] consumeItemTable = {
            { 57, 100 },    // +0 -> +1
            { 57, 500 }     // +1 -> +2
    };

    @Override
    public boolean useItem(final Playable playable, final ItemInstance item, final boolean ctrl) {
        if (playable == null || !playable.isPlayer()) {
            return false;
        }
        final Player player = (Player) playable;
        final int enchantLevel = item.getEnchantLevel();
        if (consumeItemTable.length <= enchantLevel) {
            player.sendMessageLang(
                    "Предмет достиг максимального уровня",
                    "You cannot upgrade this item further"
            );
            return false;
        }

        String val = consumeItemTable[enchantLevel][1] + " " + ItemTemplateHolder.getInstance().getTemplate(consumeItemTable[enchantLevel][0]).getName();
        String text = player.isLangRus() ? "Для улучшения сертификата Вам понадобится " + val : "Upgrade of this buff certificate will costs you " + val;
        player.certificateUpgradeRequest(text, item.getObjectId(), consumeItemTable[enchantLevel][0], consumeItemTable[enchantLevel][1]);
        return true;
    }

    @Override
    public int[] getItemIds() {
        return itemIdList;
    }
}
