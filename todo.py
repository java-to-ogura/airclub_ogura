import json
import os
import sys

DATA_FILE = 'tasks.json'

def load_tasks():
    if os.path.exists(DATA_FILE):
        with open(DATA_FILE, 'r', encoding='utf-8') as f:
            return json.load(f)
    return []

def save_tasks(tasks):
    with open(DATA_FILE, 'w', encoding='utf-8') as f:
        json.dump(tasks, f, ensure_ascii=False, indent=2)

def list_tasks():
    tasks = load_tasks()
    if not tasks:
        print('No tasks.')
    else:
        for idx, t in enumerate(tasks, 1):
            status = '✓' if t.get('done') else ' '
            print(f"{idx}. [{status}] {t['text']}")

def add_task(text):
    tasks = load_tasks()
    tasks.append({'text': text, 'done': False})
    save_tasks(tasks)
    print('Task added.')

def complete_task(index):
    tasks = load_tasks()
    if 0 <= index < len(tasks):
        tasks[index]['done'] = True
        save_tasks(tasks)
        print('Task completed.')
    else:
        print('Invalid task number.')

def delete_task(index):
    tasks = load_tasks()
    if 0 <= index < len(tasks):
        tasks.pop(index)
        save_tasks(tasks)
        print('Task deleted.')
    else:
        print('Invalid task number.')

def usage():
    print("Usage: python todo.py [list|add TEXT|done NUMBER|delete NUMBER]")

if __name__ == '__main__':
    if len(sys.argv) < 2:
        usage()
    else:
        cmd = sys.argv[1]
        if cmd == 'list':
            list_tasks()
        elif cmd == 'add':
            if len(sys.argv) < 3:
                print('Provide task text.')
            else:
                add_task(' '.join(sys.argv[2:]))
        elif cmd == 'done':
            if len(sys.argv) < 3:
                print('Provide task number.')
            else:
                try:
                    index = int(sys.argv[2]) - 1
                    complete_task(index)
                except ValueError:
                    print('Provide a number.')
        elif cmd == 'delete':
            if len(sys.argv) < 3:
                print('Provide task number.')
            else:
                try:
                    index = int(sys.argv[2]) - 1
                    delete_task(index)
                except ValueError:
                    print('Provide a number.')
        else:
            usage()
