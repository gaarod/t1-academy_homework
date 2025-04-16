ALTER TABLE tasks
    ADD status VARCHAR(255) DEFAULT 'TO_DO',
    ADD CONSTRAINT tasks_status_check CHECK (
        status IN ('TO_DO', 'IN_PROGRESS', 'DONE')
    );