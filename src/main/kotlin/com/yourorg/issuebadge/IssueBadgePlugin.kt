package com.yourorg.issuebadge

import com.intellij.openapi.actionSystem.AnAction
import com.intellij.openapi.actionSystem.AnActionEvent
import com.intellij.openapi.ui.Messages

class IssueBadgePlugin : AnAction() {
  override fun actionPerformed(e: AnActionEvent) {
    val key = BadgeService.getApiKey() ?: run {
      val input = Messages.showInputDialog("Enter IssueBadge API Key:", "API Key", null)
      if (input.isNullOrBlank()) return
      BadgeService.saveApiKey(input)
      input
    }

    val badges = BadgeService.fetchBadges(key)
    val selected = Messages.showEditableChooseDialog(
      "Select badge:",
      "IssueBadge",
      null,
      badges.map { it.name }.toTypedArray(),
      badges.first().name,
      null
    ) ?: return

    val recipient = Messages.showInputDialog("Recipient Name:", "Send Badge", null) ?: return
    val email = Messages.showInputDialog("Recipient Email:", "Send Badge", null) ?: return

    val idemp = BadgeService.sendBadge(key, selected, recipient, email)
    Messages.showInfoMessage("Sent! ID key = $idemp", "Success")
  }
}