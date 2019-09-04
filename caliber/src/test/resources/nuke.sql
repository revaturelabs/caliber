/*
Destroy everything
Obliterate what makes us weak
Destroy everything
Decimate what threatens me

Cleanse this world with flame
End this, cleanse this
Rebuild and start again
Obliterate what makes us weak
*/

drop table caliber_address cascade constraints;
drop table caliber_assessment cascade constraints;
drop table caliber_batch cascade constraints;
drop table caliber_category cascade constraints;
drop table caliber_grade cascade constraints;
drop table caliber_note cascade constraints;
drop table caliber_panel cascade constraints;
drop table caliber_panel_feedback cascade constraints;
drop table caliber_task cascade constraints;
drop table caliber_task_completion cascade constraints;
drop table caliber_trainee cascade constraints;
drop table caliber_trainer cascade constraints;
drop sequence address_id_sequence;
drop sequence assessment_id_sequence;
drop sequence batch_id_sequence;
drop sequence category_id_sequence;
drop sequence grade_id_sequence;
drop sequence note_id_sequence;
drop sequence panel_feedback_id_sequence;
drop sequence panel_id_sequence;
drop sequence task_completion_id_sequence;
drop sequence task_id_sequence;
drop sequence trainee_id_sequence;
drop sequence trainer_id_sequence;