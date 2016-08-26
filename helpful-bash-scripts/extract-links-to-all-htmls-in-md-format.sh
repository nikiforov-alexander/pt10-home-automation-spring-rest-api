#!/bin/bash
#/fsnfs/users/nikiforo/scripts/not-chemical/markdown-helpers 
#                            initialization and helper 
filename=$0

#                            check if library with my functions is defined
if [ -z $s ] ; then
    library_with_my_funcs=`find . -name "ee.sh"`
else
    library_with_my_funcs="$s/ee.sh"
fi
if [ ! -f $library_with_my_funcs ] ; then 
    em library_with_my_funcs not found && exit 1
fi
echo source $library_with_my_funcs
source $library_with_my_funcs
#                            

#                            helper method printed when -h option is called
helper () { _
    em $filename
    filename_tail=`echo $filename | awk -F"/" '{print $NF}'`
    grep -B 1 "$filename_tail\"$" $aliases_scripts | head -1
    em possible options are 
    em -h: "help"    - print this information and exit
    em -v: "vim"     - vim this script file
    em -n: "name"    - prints the name of script file
    em '[0]args': you will bew questioned about everything
    em '[n]args': '$1 ...'
_; }

#                            getopts 
while getopts  hvni: opt ; do
    case $opt in
        h) helper     && exit 0 ;;  
        v) ss_and_vim && exit 0 ;;
        n) echo $filename && exit 0 ;;
        i)
            em dummy argument $OPTARG 	
            exit 0
        ;;
        *)  em invalid option && exit 1 ;;  
    esac
    opts=1
done

#                            functions 
parse_args () { _
    if [ -z $opts ] ; then
        case $# in	
            0)
                em 0 args
            ;;
            *)
                case $1 in 
                    h) helper     && exit 0 ;;
                    v) ss_and_vim && exit 0 ;;
                    *)
                        em not implemented && exit 0
                    ;;
                esac
            ;;
        esac
    fi
_; } 
set_init_vars () { _
    var pwd $PWD -set_tail
    var list_of_manually_defined_files_to_be_converted_to_links \
        `find . -name "list_of_manually_defined_files_to_be_converted_to_links.list"` \
        || exit 1 
_; } 

extract_files_to_markdown_links_with_file_extension () { _ $@
    parse_args () { _ $@
        var file_extention $1 || exit 1
    _; } 
    set_init_vars () { _
        var output_md \
            "links_of_${file_extention}_files.md" \
            -create_empty_file 
    _; } 
        set_init_vars_for_specific_link () { _
            var file_tail `echo $file | awk -F"/" '{print $NF}'`
            var link_path $file
            #var link_name `echo $file_tail \
            #    | sed "s/\./_/g" \
            #    | sed "s/-/_/g"` || exit 1
            var link_name $file_tail
        _; } 
        write_link_to_output_md () { _
            {
                echo "[$link_name]:" 
                echo "    $link_path \"$file\""
            } >> $output_md
        _; } 
    #                            body                           #   
    parse_args $@
    set_init_vars
    for file in `find ./src -name "*.$file_extention"` ; do
        set_init_vars_for_specific_link
        write_link_to_output_md
    done
    cf $output_md
    #                            end                            #   
_; } 
extract_files_to_markdown_links_without_file_extention () { _ $@
    parse_args () { _ $@
        var file_extention $1 || exit 1
    _; } 
    set_init_vars () { _
        var output_md \
            "links_of_${file_extention}_files-without_file_extension.md" \
            -create_empty_file 
    _; } 
        set_init_vars_for_specific_link () { _
            var file_tail `echo $file | awk -F"/" '{print $NF}'`
            var link_path $file
            var link_name \
                `echo $file_tail | awk -F"." '{print $1}'` \
                || exit 1
        _; } 
        write_link_to_output_md () { _
            {
                echo "[$link_name]:" 
                echo "    $link_path \"$file\""
            } >> $output_md
        _; } 
    #                            body                           #   
    parse_args $@
    set_init_vars
    for file in `find ./src -name "*.$file_extention"` ; do
        set_init_vars_for_specific_link
        write_link_to_output_md
    done
    cf $output_md
    #                            end                            #   
_; } 

extract_various_common_files () { _
    set_init_vars () { _
        var output_md \
            "links_of_various_manually_defined_files.md" \
            -create_empty_file 
    _; } 
    extract_one_file_link_to_markdown_link_with_file_extension () { _ $@
        set_init_vars_for_specific_link () { _
            var file_extention \
                `echo $file_name \
                    | awk -F"/" '{print $NF}' \
                    | awk -F"." '{print $NF}'`\
                || exit 1
            var file_tail `echo $file_name | awk -F"/" '{print $NF}'`
            var link_path "$file_name"
            var link_name $file_tail
        _; } 
        write_link_to_output_md () { _
            {
                echo "[$link_name]:" 
                echo "    $link_path \"$link_path\""
            } >> $output_md
        _; } 
        #                            body                           #   
        for file_name in `find . -name "$1"` ; do
            set_init_vars_for_specific_link
            write_link_to_output_md
        done
        cf $output_md
        #                            end                            #   
    _; } 
    #                            body                           #   
    set_init_vars
    for file_name in \
        `cat $list_of_manually_defined_files_to_be_converted_to_links` ; do
        extract_one_file_link_to_markdown_link_with_file_extension $file_name
    done
    #                            end                            #   
_; } 
#                         #  body #                         #  
set_init_vars 

parse_args $@ 

#extract_files_to_markdown_links_with_file_extension "html"
#extract_files_to_markdown_links_without_file_extention "java"
extract_various_common_files
#                         #  end #                         #  
