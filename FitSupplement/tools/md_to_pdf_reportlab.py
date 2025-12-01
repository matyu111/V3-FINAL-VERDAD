from reportlab.lib.pagesizes import A4
from reportlab.lib.styles import getSampleStyleSheet, ParagraphStyle
from reportlab.platypus import SimpleDocTemplate, Paragraph, Spacer
from reportlab.lib.units import mm
import textwrap
import re

md_path = 'docs/ERS_UPDATED.md'
pdf_path = 'docs/ERS_UPDATED.pdf'

# Simple markdown-to-paragraph converter (headings and lists supported)

def parse_markdown_lines(lines):
    elements = []
    styles = getSampleStyleSheet()
    h1 = ParagraphStyle('Heading1', parent=styles['Heading1'], spaceAfter=6)
    h2 = ParagraphStyle('Heading2', parent=styles['Heading2'], spaceAfter=4)
    normal = ParagraphStyle('Normal', parent=styles['BodyText'], spaceAfter=4)
    bullet = ParagraphStyle('Bullet', parent=styles['BodyText'], leftIndent=12, bulletIndent=6, spaceAfter=2)

    i = 0
    while i < len(lines):
        line = lines[i].rstrip('\n')
        if not line.strip():
            i += 1
            continue
        # Heading 1
        if line.startswith('# '):
            text = line[2:].strip()
            elements.append(Paragraph(text, h1))
            elements.append(Spacer(1, 4))
            i += 1
            continue
        # Heading 2
        if line.startswith('## '):
            text = line[3:].strip()
            elements.append(Paragraph(text, h2))
            elements.append(Spacer(1, 4))
            i += 1
            continue
        # Bullet list
        if re.match(r'^[\-\*]\s+', line):
            # gather contiguous bullets
            bullets = []
            while i < len(lines) and re.match(r'^[\-\*]\s+', lines[i]):
                bullets.append(lines[i].lstrip('-* ').rstrip())
                i += 1
            for b in bullets:
                elements.append(Paragraph('&#9679; ' + b, bullet))
            elements.append(Spacer(1, 4))
            continue
        # Ordered list
        if re.match(r'^\d+\.\s+', line):
            nums = []
            while i < len(lines) and re.match(r'^\d+\.\s+', lines[i]):
                nums.append(lines[i].lstrip('0123456789. ').rstrip())
                i += 1
            idx = 1
            for n in nums:
                elements.append(Paragraph(f'{idx}. {n}', bullet))
                idx += 1
            elements.append(Spacer(1, 4))
            continue
        # Paragraph (wrap long lines)
        # Collect following non-empty lines until blank
        para_lines = [line]
        i += 1
        while i < len(lines) and lines[i].strip():
            para_lines.append(lines[i].rstrip('\n'))
            i += 1
        para_text = ' '.join(l.strip() for l in para_lines)
        # Replace markdown inline code/backticks with monospace-looking text (simple)
        para_text = para_text.replace('`', '')
        # basic bold/italic removal of markdown markers to avoid literal **
        para_text = para_text.replace('**', '').replace('__', '').replace('*', '')
        # Escape special characters for reportlab Paragraph
        para_text = para_text.replace('&', '&amp;')
        elements.append(Paragraph(para_text, normal))
        elements.append(Spacer(1, 6))
    return elements


def main():
    with open(md_path, 'r', encoding='utf-8') as f:
        lines = f.readlines()
    doc = SimpleDocTemplate(pdf_path, pagesize=A4, rightMargin=20*mm, leftMargin=20*mm, topMargin=20*mm, bottomMargin=20*mm)
    elems = parse_markdown_lines(lines)
    doc.build(elems)
    print('PDF generado en:', pdf_path)

if __name__ == '__main__':
    main()
