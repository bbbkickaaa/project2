<template>
  <div v-if="editor">
    <editor-menu-bar :editor="editor" class="editor-toolbar" >

      <select v-model="selectedHeadingSize" class="editor-select">
      <option value="3">매우 크게</option>
      <option value="4">크게</option>
      <option value="5">조금 크게</option>
      <option value="6">보통</option>
    </select>

      <button class="editor-button" @click="editor.chain().focus().toggleBold().run()" :disabled="!editor.can().chain().focus().toggleBold().run()" :class="{ 'is-active': editor.isActive('bold') }">
        <span class="material-symbols-outlined">format_bold</span>
    </button>
    <button class="editor-button" @click="editor.chain().focus().toggleItalic().run()" :disabled="!editor.can().chain().focus().toggleItalic().run()" :class="{ 'is-active': editor.isActive('italic') }">
      <span class="material-symbols-outlined">format_italic</span>
    </button>
    <button class="editor-button" @click="editor.chain().focus().toggleStrike().run()" :disabled="!editor.can().chain().focus().toggleStrike().run()" :class="{ 'is-active': editor.isActive('strike') }">
      <span class="material-symbols-outlined">format_strikethrough</span>
    </button>
    <button class="editor-button" @click="editor.chain().focus().toggleCode().run()" :disabled="!editor.can().chain().focus().toggleCode().run()" :class="{ 'is-active': editor.isActive('code') }">
      <span class="material-symbols-outlined">code</span>
    </button>




    <button class="editor-button" @click="editor.chain().focus().toggleBulletList().run()" :class="{ 'is-active': editor.isActive('bulletList') }">
      <span class="material-symbols-outlined">format_list_bulleted</span>
    </button>
    <button class="editor-button" @click="editor.chain().focus().toggleOrderedList().run()" :class="{ 'is-active': editor.isActive('orderedList') }">
      <span class="material-symbols-outlined">format_list_numbered</span>
    </button>
    <button class="editor-button" @click="editor.chain().focus().toggleCodeBlock().run()" :class="{ 'is-active': editor.isActive('codeBlock') }">
      <span class="material-symbols-outlined">code_blocks</span>
    </button>
    <button class="editor-button" @click="editor.chain().focus().undo().run()" :disabled="!editor.can().chain().focus().undo().run()">
      <span class="material-symbols-outlined">undo</span>
    </button>
    <button class="editor-button" @click="editor.chain().focus().redo().run()" :disabled="!editor.can().chain().focus().redo().run()">
      <span class="material-symbols-outlined">redo</span>
    </button>
    <button class="editor-button" @click="editor.chain().focus().setTextAlign('left').run()" :class="{ 'is-active': editor.isActive({ textAlign: 'left' }) }">
      <span class="material-symbols-outlined">format_align_left</span>
    </button>
    <button class="editor-button" @click="editor.chain().focus().setTextAlign('center').run()" :class="{ 'is-active': editor.isActive({ textAlign: 'center' }) }">
      <span class="material-symbols-outlined">format_align_center</span>
    </button>
    <button class="editor-button" @click="editor.chain().focus().setTextAlign('right').run()" :class="{ 'is-active': editor.isActive({ textAlign: 'right' }) }">
      <span class="material-symbols-outlined">format_align_right</span>
    </button>
    <button class="editor-button" @click="editor.chain().focus().toggleHighlight().run()" :class="{ 'is-active': editor.isActive('highlight') }">
      <span class="material-symbols-outlined">format_ink_highlighter</span>
    </button>
    

      <!-- 기타 다른 버튼들도 이와 유사하게 추가 -->
    </editor-menu-bar>
    <editor-content :editor="editor"   class="editor-container"/>
  </div>
</template>

<script setup>
import { useEditor, EditorContent } from '@tiptap/vue-3'
import StarterKit from '@tiptap/starter-kit'
import {watch , ref } from 'vue'
import TextAlign from '@tiptap/extension-text-align'
import Highlight from '@tiptap/extension-highlight'
import FontFamily from '@tiptap/extension-font-family'
import Text from '@tiptap/extension-text'
import TextStyle from '@tiptap/extension-text-style'
const props = defineProps({
  modelValue: {
    type: String,
    default: ''
  }
});

const emit = defineEmits(['update:modelValue']);

const editor = useEditor({
  content: props.modelValue,
  extensions: [
        StarterKit,
        TextAlign.configure({
          types: ['heading', 'paragraph'],
        }),
        Highlight,
        FontFamily.configure({
            types: ['textStyle'],
          }),
          Text,
        TextStyle,
      ],
  onUpdate : () => {
    emit('value',editor.value.getHTML());
  },
});

watch (()=> props.modelValue, value => {
const isSame = editor.value.getHTML() === value;
if(isSame){ return;}
editor.value.commands.SVGTextContentElement(value,false);}
);

const selectedHeadingSize = ref('6');
watch(selectedHeadingSize, (newSize) => {
  editor.value.chain().focus().toggleHeading({ level: parseInt(newSize) }).run();
});
</script>

<style lang="scss">
.editor-button {
  background-color: #f5f5f5;
  border: 1px solid #ddd;
  border-radius: 4px;
  padding: 8px;
  margin-right: 4px;
  cursor: pointer;
}
.editor-button:hover {
  background-color: #eaeaea;
}
.editor-button.is-active {
  background-color: #d5d5d5;
}


.material-symbols-outlined {
  font-size: 20px;
  color: #333;
}

.editor-toolbar {
  display: flex;
  flex-wrap: wrap;
  gap: 8px;
  align-items: center;
}

.editor-select {
  height : 43px;
  padding: 8px;
  margin-right: 4px;
  border: 1px solid #ddd;
  border-radius: 4px;
  background-color: #f5f5f5;
  cursor: pointer;
}

.editor-select:hover {
  background-color: #eaeaea;
}


.ProseMirror {
  padding: 30px;
  min-height: 650px;
  margin-bottom : 50px;
  margin-top: 20px;
  border-top : solid 1px #999999; 
  border-bottom : solid 1px #999999; 
}
/* Basic editor styles */
.tiptap {
  > * + * {
    margin-top: 0.75em;
  }

  ul,
  ol {
    padding: 0 1rem;
  }

  h1,
  h2,
  h3,
  h4,
  h5,
  h6 {
    line-height: 1.1;
  }

  code {
    background-color: rgba(#616161, 0.1);
    color: #616161;
  }

  pre {
    background: #0D0D0D;
    color: #FFF;
    font-family: 'JetBrainsMono', monospace;
    padding: 0.75rem 1rem;
    border-radius: 0.5rem;

    code {
      color: inherit;
      padding: 0;
      background: none;
      font-size: 0.8rem;
    }
  }

  img {
    max-width: 100%;
    height: auto;
  }

  blockquote {
    padding-left: 1rem;
    border-left: 2px solid rgba(#0D0D0D, 0.1);
  }

  hr {
    border: none;
    border-top: 2px solid rgba(#0D0D0D, 0.1);
    margin: 2rem 0;
  }
}
</style>