Alarm app<br>
<br>
База проекта mvi, clean architecture<br>
<br>
📦 app<br>
┣━━ DI (инициализация зависимостей)<br>
<br>
📦 core<br>
┣━━ common<br>
┃    ┣━━ Result<br>
┃    ┣━━ Extensions<br>
┃    ┣━━ Logging<br>
┃    ┗━━ Utils<br>
┣━━ ui<br>
┃    ┣━━ UI Components<br>
┃    ┗━━ 
<br>
📦 data<br>
┣━━ alarm<br>
┃    ┣━━ AlarmDao (Room)<br>
┃    ┣━━ AlarmRepositoryImpl<br>
┃    ┣━━ AlarmEntity<br>
┃    ┣━━ DataSource<br>
┃    ┣━━ NetworkClient (later)<br>
┃    ┗━━ AlarmMapper (для преобразования между Entity и Domain)<br>
<br>
📦 domain<br>
┣━━ alarm<br>
┃    ┣━━ models (Alarm)<br>
┃    ┣━━ usecases<br>
┃    ┃    ┣━━ SelectAudioFileUseCase<br>
┃    ┃    ┣━━ GetAlarmByIdUseCase<br>
┃    ┃    ┣━━ InsertAlarmUseCase<br>
┃    ┃    ┣━━ UpdateAlarmUseCase<br>
┃    ┃    ┣━━ DeleteAlarmUseCase<br>
┃    ┃    ┗━━ GetAllAlarmsUseCase<br>
┃    ┣━━ repositories<br>
┃    ┃    ┗━━ AlarmRepository<br>
┃    ┗━━ AlarmMapper (если нет в data)<br>
<br>
📦 feature<br>
┣━━ alarm(alarm edit)<br>
┃    ┣━━ UI <br>
┃    ┣━━ ViewModel<br>
┃    ┣━━ MVI (State, Intent, Reducer(maybe later))<br>
┃    ┗━━ UI components<br>
<br>
📦 feature<br>
┣━━ alarm_list<br>
┃    ┣━━ UI <br>
┃    ┣━━ ViewModel<br>
┃    ┣━━ MVI (State, Intent, Reducer(maybe later))<br>
┃    ┗━━ UI components<br>
<br>
📦 feature<br>
┣━━ fired_alarm (components for alarm wake up) <br>
┃    ┣━━ UI <br>
┃    ┣━━ ViewModel<br>
┃    ┣━━ MVI (State, Intent, Reducer(maybe later))<br>
┃    ┗━━ UI components<br>
<br>
📦 service<br>
┣━━ alarm<br>
┃    ┣━━ AlarmReceiver<br>
┃    ┣━━ AlarmService<br>
┃    ┣━━ NotificationUtils<br>
┃    ┣━━ AlarmScheduler <br>
┃    ┗━━ ForegroundService #   A l a r m <br>
 
 
