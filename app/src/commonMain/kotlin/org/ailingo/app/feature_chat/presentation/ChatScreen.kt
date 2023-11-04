package org.ailingo.app.feature_chat.presentation

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material3.BottomAppBar
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import cafe.adriel.voyager.core.screen.Screen
import org.ailingo.app.core.util.VoiceToTextParser
import org.ailingo.app.feature_chat.data.model.Message

data class ChatScreen(val voiceToTextParser: VoiceToTextParser) : Screen {

    @Composable
    override fun Content() {
        val voiceState = voiceToTextParser.voiceState.collectAsState()
        val textField = remember {
            mutableStateOf("")
        }
        val messages = remember { mutableStateListOf<Message>() }

        LaunchedEffect(Unit) {
            messages.addAll(
                listOf(
                    Message("Привет, как я могу вам помочь?", false),
                    Message(
                        "Привет! Я очень заинтересован в изучении нового языка. У меня много вопросов о том, как начать и как сделать процесс изучения более эффективным.",
                        true
                    ),
                    Message(
                        "Это замечательно, что вы решили изучать новый язык! Первый шаг - это определить цель изучения языка. Хотите вы научиться разговаривать на повседневные темы или подготовиться к экзамену? Понимание вашей цели поможет выбрать подходящие методы обучения.",
                        false
                    ),
                    Message(
                        "Да, я хочу освоить разговорный язык для путешествий и общения с носителями языка. Как я могу начать?",
                        true
                    ),
                    Message(
                        "Отлично! Начните с основ. Изучите базовые фразы, числа, дни недели и основные грамматические правила. Попробуйте найти языкового партнера или присоединитесь к языковой группе, где вы сможете практиковать разговор на новом языке. Также рекомендуется слушать музыку, смотреть фильмы и читать книги на изучаемом языке для погружения в языковую среду.",
                        false
                    ),
                    Message(
                        "Это звучит интересно! А как насчет грамматики? Я всегда путаюсь в грамматических правилах нового языка.",
                        true
                    ),
                    Message(
                        "Не переживайте из-за грамматики - это нормально на начальном этапе. Постепенно погружайтесь в грамматические правила, начиная с простых и постепенно переходите к более сложным. Практикуйте грамматику через разговоры и письменные упражнения. Со временем, с практикой, вы начнете понимать и запоминать грамматические конструкции.",
                        false
                    ),
                    Message(
                        "Спасибо за советы! Я попробую ваши рекомендации и начну свое языковое путешествие. Если у меня возникнут еще вопросы, я могу обратиться к вам?",
                        true
                    ),
                    Message(
                        "Конечно, всегда рад помочь! Удачи в изучении языка. Не стесняйтесь обращаться, если у вас будут вопросы или вам понадобится помощь. Всего наилучшего в вашем языковом путешествии!",
                        false
                    )
                )
            )
        }

        val listState = rememberLazyListState()
        var lastSpokenText by remember { mutableStateOf("") }

        LaunchedEffect(!voiceState.value.isSpeaking && voiceState.value.spokenText.isNotEmpty() && voiceState.value.spokenText != lastSpokenText) {
            textField.value = voiceState.value.spokenText
            lastSpokenText = voiceState.value.spokenText
        }

        Scaffold(
            bottomBar = {
                BottomAppBar {
                    BottomUserMessageBox(
                        textField,
                        voiceToTextParser,
                        voiceState,
                        messages,
                        listState
                    )
                }
            }
        ) { padding ->
            LazyColumn(
                state = listState,
                modifier = Modifier
                    .fillMaxSize()
                    .padding(padding)
            ) {
                items(messages) { message ->
                    MessageItem(message)
                }
            }
        }
    }

}
