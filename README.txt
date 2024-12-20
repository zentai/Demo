# JiraStreamline

## Overview
This tool automates the transformation of Excel data into Drafted Jira Tickets, significantly reducing repetitive tasks and saving hours of manual effort. Designed to handle large-scale requirements, it efficiently processes multiple rows in a single Excel file, generates pre-configured tickets, and supports automatic linking to improve team collaboration and efficiency.

## Design Principles
1. **Data Structuring**:
   - Streamlines Excel data into a consistent structure for fast retrieval and analysis, ensuring accurate mappings of requirements.
   - Supports hierarchical datasets, enabling efficient processing of complex data structures.
   - Offers flexible outputs: structured definitions for quick reviews and Markdown tables for detailed visualization. 

1. **Template-driven Flexibility**:
   - Leverages existing Jira tickets as reusable templates, enabling quick adaptation to project-specific needs.
   - Automates repetitive configurations while allowing users to customize key fields, ensuring both efficiency and uniformity. 

## Business Value and Benefits

### Background
Our team has a well-established software development framework, where each Data Field is accompanied by detailed and independent analysis and development documents. This approach significantly reduces the risk of knowledge loss. However, creating these documents manually at the start of a project is time-intensive, requiring significant effort from BAs and resulting in high labor costs. This tool addresses these challenges by automating repetitive tasks, transforming them into a streamlined one-time setup.

1. **Efficiency and Cost Savings**:  
   - Automates tedious tasks like ticket creation, reducing manual operations by over 70% and minimizing errors.  
   - Frees up valuable time and resources, allowing teams to focus on high-priority strategic activities like analysis and decision-making.

2. **Accelerated Start-up with Quantifiable Impact**:  
   - Shortens project preparation time by automating ticket generation and ensuring consistency across tickets.  
   - For example, processing a single row in an Excel file generates 1 Feature Requirement (FR), 1 Development Ticket (Dev), and 2 Dev Sub-tickets.  
   - A file with 500 rows translates to 2000 linked Jira tickets, saving nearly 192 hours (equivalent to 4 full work weeks) compared to manual creation, reducing ticket generation time from 6 minutes per ticket to just 30 seconds.

## User Guide

1. **Prepare Data**:  
   - Ensure Excel files are correctly structured with complete data, and verify that all required headers and fields are present.  
   - Multi-level headers are transformed into a single structured hierarchy by joining layers with `+` to form unique keys.  
     *E.g., headers like `Technical Requirement` (row 0), `Trade` (row 1), and `FX` (row 2) are flattened into the key `Technical Requirement+Trade+FX`.*  
   - By default, the tool returns all flattened headers and their corresponding values unless specific fields are specified.  
     *E.g., Without specifying fields, all headers such as `Technical Requirement+Trade+FX` and `Technical Requirement+Trade+CO` are returned with their values.*  
   - Use wildcard rules, such as `Technical Requirement+Trade+*`, to specify only relevant fields for inclusion or aggregation.  

2. **Configure Templates**:  
   - Adjust template content to fit project needs, leveraging placeholders in the format `${xxx xxx}` to dynamically populate fields based on Excel data.  
     *E.g., `${Technical Requirement+Trade+FX}` pulls data from the column `Technical Requirement > Trade > FX` in Excel and inserts it into the corresponding ticket field.*  
   - Use wildcard rules (e.g., `${Technical Requirement+Trade+*}`) for aggregating matching subfields into structured tables.  
   - Templates allow conditional logic for advanced content generation:  
     *E.g., `${Impacted Systems}` defaults to `"None"` if the corresponding Excel field is empty.*

3. **Dry Run for Validation**:  
   - Before creating tickets, use the `Dry Run` mode to preview how actual data from Excel populates the template.  
   - This feature ensures placeholders like `${Epic Link}` and `${Technical Requirement+Trade+FX}` are correctly mapped, reducing errors in ticket creation.  
     *E.g., A preview of the populated ticket fields helps detect mismatched or missing data before final submission.*

4. **Run the Tool**:  
   - Once templates are configured and validated via Dry Run, proceed to generate tickets directly from the tool.  

5. **Verify Results**:  
   - Review the generated tickets to confirm the accuracy of the content and links, ensuring all fields are aligned with project requirements.  



## Limitations

1. **Row-based Data Formats**:  
   This tool is optimized for data formats where each row represents a Jira tickets. 

2. **Unicode Header Limitations**:  
   The tool currently has partial support for Unicode headers, which may lead to inaccuracies when processing non-ASCII characters. Users working with multilingual datasets are advised to standardize headers to ASCII-compatible formats.

## Future Directions

1. **Batch Update Features**:  
   Introduce advanced capabilities to update existing tickets, link related tasks, and handle multi-stage workflows efficiently.

2. **Multi-project Adaptability**:  
   Enable template flexibility to accommodate diverse data structures and varying requirements across projects.

3. **Enhanced Aggregation Support**:  
   Implement new aggregation methods for summarizing hierarchical data or consolidating multiple fields into a single representation.


